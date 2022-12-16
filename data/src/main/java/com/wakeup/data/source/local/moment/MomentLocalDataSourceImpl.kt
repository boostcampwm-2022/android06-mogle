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
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.data.database.entity.SuperMomentEntity
import com.wakeup.data.database.mapper.toMomentEntity
import com.wakeup.data.util.InternalFileUtil
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow
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

    override suspend fun saveMoment(moment: SuperMomentEntity, id: Long?) {
        val momentId = if (id == null) {
            momentDao.saveMoment(moment.toMomentEntity())
        } else {
            momentDao.saveMoment(moment.toMomentEntity(id))
        }
        var savedPictures: List<PictureEntity> = listOf()
        if (moment.pictures.isNotEmpty()) {
            savedPictures = savePictures(moment.pictures)
            saveMomentPictureXRefs(momentId, savedPictures.map { savedPicture -> savedPicture.id })
        }
        val globeToSaveMoment = moment.globes.first()
        saveMomentGlobeXRef(momentId, globeToSaveMoment, savedPictures)
    }

    override fun getMoment(momentId: Long): Flow<MomentWithGlobesAndPictures> = momentDao.getMoment(momentId)

    private suspend fun savePictures(pictures: List<PictureEntity>): List<PictureEntity> {
        savePictureInternalStorage(pictures)
        val pictureLastPathFileName = getPictureEntityAboutLastPathFileNames(pictures)
        val indexResult = pictureDao.savePictures(pictureLastPathFileName)
        return getCorrectSavedPictures(indexResult, pictureLastPathFileName)
    }

    private fun savePictureInternalStorage(pictures: List<PictureEntity>) {
        pictures.forEach { picture ->
            util.savePictureInInternalStorage(picture)
        }
    }

    private fun getPictureEntityAboutLastPathFileNames(pictures: List<PictureEntity>): List<PictureEntity> {
        return pictures.map { picture -> picture.copy(path = picture.path.substringAfterLast("/")) }
    }

    private suspend fun getCorrectSavedPictures(
        pictureIds: List<Long>,
        pictures: List<PictureEntity>,
    ): List<PictureEntity> {
        val tempPictures = mutableListOf<PictureEntity>()
        pictureIds.forEachIndexed { idx, id ->
            val path = pictures[idx].path
            if (id == EXIST_INSERT_ERROR_CODE) {
                tempPictures.add(
                    PictureEntity(id = pictureDao.getPictureIdByByteArray(path), path = path)
                )
            } else {
                tempPictures.add(
                    PictureEntity(id = id, path = path)
                )
            }
        }
        return tempPictures.toList()
    }

    private suspend fun saveMomentPictureXRefs(momentId: Long, pictureIds: List<Long>) {
        val momentPictureXRefs = pictureIds.map { pictureId ->
            MomentPictureXRef(momentId = momentId, pictureId = pictureId)
        }
        xRefDao.saveMomentPictureXRefs(momentPictureXRefs)
    }

    private suspend fun saveMomentGlobeXRef(
        momentId: Long,
        globe: GlobeEntity,
        pictures: List<PictureEntity>,
    ) {
        xRefDao.saveMomentGlobeXRef(MomentGlobeXRef(momentId = momentId, globeId = globe.id))

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

    override suspend fun updateMoment(moment: SuperMomentEntity) {
        deleteMoment(moment.id)
        saveMoment(moment, moment.id)
    }

    companion object {
        const val EXIST_INSERT_ERROR_CODE = -1L
        const val PREFETCH_PAGE = 2
        const val ITEMS_PER_PAGE = 10
    }
}