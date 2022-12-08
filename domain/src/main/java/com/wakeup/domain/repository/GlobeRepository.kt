package com.wakeup.domain.repository

import androidx.paging.PagingData
import com.wakeup.domain.model.Globe
import com.wakeup.domain.model.Moment
import kotlinx.coroutines.flow.Flow

interface GlobeRepository {

    suspend fun createGlobe(globe: Globe)

    suspend fun updateGlobe(globe: Globe)

    suspend fun deleteGlobe(globe: Globe)

    fun getGlobes(): Flow<List<Globe>>

    fun getMomentsByGlobe(globeId: Long): Flow<PagingData<Moment>>

    suspend fun getFirstMomentByGlobe(globeId: Long): Moment?

    suspend fun getMomentCountByGlobe(globeId: Long): Int

    fun getMomentsNotInGlobe(globeId: Long): Flow<PagingData<Moment>>
}