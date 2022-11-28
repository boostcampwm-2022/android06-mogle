package com.wakeup.data.source.local.globe

import com.wakeup.data.database.dao.GlobeDao
import com.wakeup.data.database.entity.GlobeEntity
import kotlinx.coroutines.flow.Flow
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
        globeDao.updateGlobe(globe)
    }

    override fun getGlobes(): Flow<List<GlobeEntity>> {
        return globeDao.getGlobes()
    }
}