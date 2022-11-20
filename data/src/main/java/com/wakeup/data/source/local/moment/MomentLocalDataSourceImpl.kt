package com.wakeup.data.source.local.moment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.database.entity.PictureEntity
import kotlinx.coroutines.flow.Flow

class MomentLocalDataSourceImpl(
    private val momentDao: MomentDao,
) : MomentLocalDataSource {
    override fun getMoments(query: String, sort: String): Flow<PagingData<MomentEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { momentDao.getMoments(query) }
        ).flow

    override fun getPictures(momentId: Long): Flow<List<PictureEntity>> =
        momentDao.getPictures(momentId)

    override fun getGlobes(momentId: Long): Flow<List<GlobeEntity>> =
        momentDao.getGlobes(momentId)

    override suspend fun saveMoment(moment: MomentEntity): Long =
        momentDao.saveMoment(moment)

    override suspend fun savePicture(picture: List<PictureEntity>): List<Long> =
        momentDao.savePicture(picture)

    override suspend fun saveMomentPicture(MomentPictures: List<MomentPictureEntity>) {
        momentDao.saveMomentPicture(MomentPictures)
    }

    companion object {
        const val ITEMS_PER_PAGE = 10
    }
}