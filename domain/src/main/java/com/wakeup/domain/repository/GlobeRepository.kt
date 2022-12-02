package com.wakeup.domain.repository

import com.wakeup.domain.model.Globe
import com.wakeup.domain.model.Moment
import kotlinx.coroutines.flow.Flow

interface GlobeRepository {

    suspend fun createGlobe(globe: Globe)

    suspend fun updateGlobe(globe: Globe)

    suspend fun deleteGlobe(globe: Globe)

    suspend fun getGlobeId(globeName: String): Long

    fun getGlobes(): Flow<List<Globe>>

    fun getMomentsByGlobe(globeId: Long): Flow<List<Moment>>

    suspend fun getMomentCountByGlobe(globeId: Long): Int
}