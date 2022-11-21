package com.wakeup.data.source.local.moment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MomentLocalDataSourceImpl @Inject constructor(
    private val momentDao: MomentDao,
) : MomentLocalDataSource {
    override fun getMoments(query: String, sortType: SortType): Flow<PagingData<MomentEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false,
                initialLoadSize = ITEMS_PER_PAGE,
                prefetchDistance = PREFETCH_PAGE
            ),
            pagingSourceFactory = {
                when (sortType) {
                    SortType.MOST_RECENT -> momentDao.getMoments(query, 0)
                    SortType.OLDEST -> momentDao.getMoments(query, 1)
                    SortType.CLOSET -> momentDao.getMomentsSortByCloset(query)
                }
            }
        ).flow

    override suspend fun getPictures(momentId: Long): List<PictureEntity> =
        momentDao.getPictures(momentId)

    override suspend fun getGlobes(momentId: Long): List<GlobeEntity> =
        momentDao.getGlobes(momentId)

    override suspend fun saveMoment(moment: MomentEntity): Long =
        momentDao.saveMoment(moment)

    override suspend fun savePicture(picture: List<PictureEntity>): List<Long> =
        momentDao.savePicture(picture)

    override suspend fun saveMomentPicture(MomentPictures: List<MomentPictureEntity>) {
        momentDao.saveMomentPicture(MomentPictures)
    }

    companion object {
        const val PREFETCH_PAGE = 2
        const val ITEMS_PER_PAGE = 10
    }
}