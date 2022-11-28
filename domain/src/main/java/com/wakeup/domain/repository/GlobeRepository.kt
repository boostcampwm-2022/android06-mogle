package com.wakeup.domain.repository

import com.wakeup.domain.model.Globe
import kotlinx.coroutines.flow.Flow

interface GlobeRepository {

    suspend fun createGlobe(globe: Globe)

    suspend fun updateGlobe(globe: Globe)

    suspend fun deleteGlobe(globe: Globe)

    fun getGlobes(): Flow<List<Globe>>
}