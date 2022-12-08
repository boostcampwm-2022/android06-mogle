package com.wakeup.data.source.local.moment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.entity.MomentEntity
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

    companion object {
        const val PREFETCH_PAGE = 2
        const val ITEMS_PER_PAGE = 10
        const val EXIST_INSERT_ERROR_CODE = -1L
    }
}