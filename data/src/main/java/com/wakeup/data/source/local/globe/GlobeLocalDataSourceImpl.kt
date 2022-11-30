package com.wakeup.data.source.local.globe

import com.wakeup.data.database.dao.GlobeDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class GlobeLocalDataSourceImpl @Inject constructor(
    private val globeDao: GlobeDao
) : GlobeLocalDataSource {

    override suspend fun createGlobe(globe: GlobeEntity) {
        globeDao.createGlobe(globe)
    }

    override suspend fun deleteGlobe(globe: GlobeEntity) {
        globeDao.deleteGlobe(globe)
    }

    override suspend fun updateGlobe(globe: GlobeEntity) {
        Timber.d("updateGlobe: $globe")
        globeDao.updateGlobe(globe)
    }

    override fun getGlobes(): Flow<List<GlobeEntity>> {
        return globeDao.getGlobes()
    }

    override fun getMomentsByGlobe(globeId: Long): Flow<List<MomentWithGlobesAndPictures>> {
        return globeDao.getMomentsByGlobe(globeId)
    }

    override fun getMomentCountByGlobe(globeId: Long): Flow<Int> {
        return globeDao.getMomentCountByGlobe(globeId)
    }


}