package com.wakeup.data.source.local.globe

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wakeup.data.database.dao.GlobeDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GlobeLocalDataSourceImpl @Inject constructor(
    private val globeDao: GlobeDao,
) : GlobeLocalDataSource {

    override suspend fun createGlobe(globe: GlobeEntity) {
        globeDao.createGlobe(globe)
    }

    override suspend fun deleteGlobe(globe: GlobeEntity) {
        globeDao.deleteGlobe(globe)
    }

    override suspend fun updateGlobe(globe: GlobeEntity) {
        globeDao.updateGlobe(globe)
    }

    override suspend fun getGlobeIdByName(globeName: String): Long {
        return globeDao.getGlobeIdByName(globeName)
    }

    override fun getGlobes(): Flow<List<GlobeEntity>> {
        return globeDao.getGlobes()
    }

    override fun getMomentsByGlobe(globeId: Long): Flow<List<MomentWithGlobesAndPictures>> {
        return globeDao.getMomentsByGlobe(globeId)
    }

    override suspend fun getMomentCountByGlobe(globeId: Long): Int {
        return globeDao.getMomentCountByGlobe(globeId)
    }

    override fun getMomentsNotInGlobe(globeId: Long): Flow<PagingData<MomentWithGlobesAndPictures>> =
        Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false,
                initialLoadSize = ITEMS_PER_PAGE,
                prefetchDistance = PREFETCH_PAGE
            ),
            pagingSourceFactory = {
                globeDao.getMomentsNotInGlobe(globeId)
            }
        ).flow

    companion object {
        const val PREFETCH_PAGE = 2
        const val ITEMS_PER_PAGE = 10
    }
}