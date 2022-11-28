package com.wakeup.domain.repository

import com.wakeup.domain.model.Globe

interface GlobeRepository {

    suspend fun createGlobe(globe: Globe)

    suspend fun updateGlobe(globe: Globe)

    suspend fun deleteGlobe(globe: Globe)

    suspend fun getGlobes(): List<Globe>
}