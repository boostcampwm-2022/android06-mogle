package com.wakeup.data.source.local.moment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MomentLocalDataSourceImpl @Inject constructor(
    private val momentDao: MomentDao,
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
                    SortType.NEAREST -> momentDao.getMomentsByNearestDistance(query,
                        myLocation?.latitude,
                        myLocation?.longitude)
                    else -> momentDao.getMoments(sortType.ordinal, query)
                }
            }
        ).flow

    override suspend fun getGlobeId(globeName: String): Long {
        return momentDao.getGlobeIdByName(globeName)
    }

    override suspend fun saveMoment(moment: MomentEntity): Long {
        return momentDao.saveMoment(moment)
    }

    override suspend fun savePictures(pictures: List<PictureEntity>): List<Long> {
        val indexResult = momentDao.savePictures(pictures).toMutableList()
        indexResult.forEachIndexed { pictureIndex, id ->
            if (id == EXIST_INSERT_ERROR_CODE) {
                indexResult[pictureIndex] = momentDao.getPictureIdByByteArray(pictures[pictureIndex].fileName)
            }
        }
        return indexResult.toList()
    }

    override suspend fun saveMomentPictures(momentPictures: List<MomentPictureXRef>) {
        momentDao.saveMomentPictureXRefs(momentPictures)
    }

    override suspend fun saveGlobes(globes: List<GlobeEntity>): List<Long> {
        return momentDao.saveGlobes(globes)
    }

    override suspend fun saveMomentGlobe(momentGlobe: MomentGlobeXRef) {
        momentDao.saveMomentGlobeXRef(momentGlobe)
    }

    companion object {
        const val PREFETCH_PAGE = 2
        const val ITEMS_PER_PAGE = 10
        const val EXIST_INSERT_ERROR_CODE = -1L
    }
}