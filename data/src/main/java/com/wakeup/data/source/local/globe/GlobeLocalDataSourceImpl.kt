package com.wakeup.data.source.local.globe

import com.wakeup.data.database.dao.GlobeDao
import com.wakeup.data.database.entity.GlobeEntity
import javax.inject.Inject

class GlobeLocalDataSourceImpl @Inject constructor(
    private val globeDao: GlobeDao
) : GlobeLocalDataSource {

    override suspend fun createGlobe(globe: GlobeEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun removeGlobe(globe: GlobeEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun updateGlobe(globe: GlobeEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getGlobes(): GlobeEntity {
        TODO("Not yet implemented")
    }
}