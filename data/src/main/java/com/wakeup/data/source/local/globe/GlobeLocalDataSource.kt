package com.wakeup.data.source.local.globe

import com.wakeup.data.database.entity.GlobeEntity

interface GlobeLocalDataSource {

    suspend fun createGlobe(globe: GlobeEntity)

    suspend fun removeGlobe(globe: GlobeEntity)

    suspend fun updateGlobe(globe: GlobeEntity)

    suspend fun getGlobes(): GlobeEntity
}