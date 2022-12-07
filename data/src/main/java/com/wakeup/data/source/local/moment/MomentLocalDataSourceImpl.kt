package com.wakeup.data.source.local.moment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.dao.PictureDao
import com.wakeup.data.database.dao.XRefDao
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.data.util.InternalFileUtil
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MomentLocalDataSourceImpl @Inject constructor(
    private val momentDao: MomentDao,
    private val pictureDao: PictureDao,
    private val xRefDao: XRefDao,
    private val util: InternalFileUtil
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

    override suspend fun saveMoment(moment: MomentEntity): Long {
        return momentDao.saveMoment(moment)
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
        const val PREFETCH_PAGE = 2
        const val ITEMS_PER_PAGE = 10
        const val EXIST_INSERT_ERROR_CODE = -1L
    }
}