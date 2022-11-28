package com.wakeup.data.source.local.globe

import com.wakeup.data.database.entity.GlobeEntity
import kotlinx.coroutines.flow.Flow

interface GlobeLocalDataSource {

    suspend fun createGlobe(globe: GlobeEntity)

    suspend fun deleteGlobe(globe: GlobeEntity)

    suspend fun updateGlobe(globe: GlobeEntity)

    fun getGlobes(): Flow<List<GlobeEntity>>
}