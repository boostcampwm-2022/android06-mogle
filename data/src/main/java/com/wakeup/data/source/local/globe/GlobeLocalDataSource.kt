package com.wakeup.data.source.local.globe

import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import kotlinx.coroutines.flow.Flow

interface GlobeLocalDataSource {

    suspend fun createGlobe(globe: GlobeEntity)

    suspend fun deleteGlobe(globe: GlobeEntity)

    suspend fun updateGlobe(globe: GlobeEntity)

    suspend fun getGlobeIdByName(globeName: String): Long

    fun getGlobes(): Flow<List<GlobeEntity>>

    fun getMomentsByGlobe(globeId: Long): Flow<List<MomentWithGlobesAndPictures>>

    fun getMomentCountByGlobe(globeId: Long): Flow<Int>
}