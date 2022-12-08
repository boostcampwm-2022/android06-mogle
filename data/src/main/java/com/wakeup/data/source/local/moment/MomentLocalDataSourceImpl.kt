package com.wakeup.data.source.local.moment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wakeup.data.database.dao.GlobeDao
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.dao.PictureDao
import com.wakeup.data.database.dao.XRefDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.data.database.entity.SuperMomentEntity
import com.wakeup.data.database.mapper.toMomentEntity
import com.wakeup.data.util.InternalFileUtil
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class MomentLocalDataSourceImpl @Inject constructor(
    private val momentDao: MomentDao,
    private val pictureDao: PictureDao,
    private val globeDao: GlobeDao,
    private val xRefDao: XRefDao,
    private val util: InternalFileUtil,
) : MomentLocalDataSource {
    override fun getMoments(
        sortType: SortType,
        query: String,
        myLocation: LocationEntity?,
    ): Flow<PagingData<MomentWithGlobesAndPictures>> =
        Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false,
                initialLoadSize = ITEMS_PER_PAGE,
                prefetchDistance = PREFETCH_PAGE
            ),
            pagingSourceFactory = {
                when (sortType) {
                    SortType.NEAREST -> momentDao.getMomentsByNearestDistance(
                        query,
                        myLocation?.latitude,
                        myLocation?.longitude
                    )
                    else -> momentDao.getMoments(sortType.ordinal, query)
                }
            }
        ).flow

    override fun getAllMoments(query: String): Flow<List<MomentWithGlobesAndPictures>> =
        momentDao.getAllMoments(query)

    override suspend fun saveMoment(moment: SuperMomentEntity) {
        val momentId = momentDao.saveMoment(moment.toMomentEntity())
        if (moment.pictures.isNotEmpty()) {
            val pictureIds = savePictures(moment.pictures)
            saveMomentPictureXRefs(momentId, pictureIds)
        }
        val savedMoment = momentDao.getMoment(momentId).moment
        val globeToSaveMoment = moment.globes.first()
        saveMomentGlobeXRef(moment.pictures, savedMoment, globeToSaveMoment)

    }

    private suspend fun savePictures(pictures: List<PictureEntity>): List<Long> {
        savePictureInternalStorage(pictures)
        val indexResult = pictureDao.savePictures(
            getPictureEntityAboutLastPathFileNames(pictures)
        )
        return getCorrectSavedPictureIds(indexResult, pictures)
    }

    private fun savePictureInternalStorage(pictures: List<PictureEntity>) {
        Timber.d("$pictures")
        pictures.forEach { picture ->
            util.savePictureInInternalStorage(picture)
        }
    }

    private fun getPictureEntityAboutLastPathFileNames(pictures: List<PictureEntity>): List<PictureEntity> {
        return pictures.map { picture -> picture.copy(path = picture.path.substringAfterLast("/")) }
    }

    private suspend fun getCorrectSavedPictureIds(
        pictureIds: List<Long>,
        pictures: List<PictureEntity>,
    ): List<Long> {
        val changeablePictureIds = pictureIds.toMutableList()
        changeablePictureIds.forEachIndexed { pictureIndex, id ->
            if (id == EXIST_INSERT_ERROR_CODE) {
                changeablePictureIds[pictureIndex] =
                    pictureDao.getPictureIdByByteArray(pictures[pictureIndex].path)
            }
        }
        return changeablePictureIds.toList()
    }

    private suspend fun saveMomentPictureXRefs(momentId: Long, pictureIds: List<Long>) {
        val momentPictureXRefs =
            pictureIds.map { pictureId -> MomentPictureXRef(momentId, pictureId) }
        xRefDao.saveMomentPictureXRefs(momentPictureXRefs)
    }

    private suspend fun saveMomentGlobeXRef(
        pictures: List<PictureEntity>,
        moment: MomentEntity,
        globe: GlobeEntity,
    ) {
        xRefDao.saveMomentGlobeXRef(MomentGlobeXRef(moment.id, globe.id))

        if (globe.thumbnail == null && pictures.isNotEmpty()) {
            globeDao.updateGlobe(globe.copy(thumbnail = pictures.first()))
        }
    }

    override suspend fun deleteMoment(momentId: Long) {
        val pictures = momentDao.getMomentPictures(momentId)
        pictures.forEach { picture ->
            val isDelete = xRefDao.isOnlyOnePicture(picture.id)
            if (isDelete) {
                util.deletePictureInInternalStorage(picture.path)
                pictureDao.deletePicture(picture.id)
            }
        }
        momentDao.deleteMoment(momentId)
    }

    companion object {
        const val EXIST_INSERT_ERROR_CODE = -1L
        const val PREFETCH_PAGE = 2
        const val ITEMS_PER_PAGE = 10
    }
}