package com.wakeup.data.source.local.globe

import androidx.paging.PagingData
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import kotlinx.coroutines.flow.Flow

interface GlobeLocalDataSource {

    suspend fun createGlobe(globe: GlobeEntity)

    suspend fun deleteGlobe(globe: GlobeEntity)

    suspend fun updateGlobe(globe: GlobeEntity)

    fun getGlobes(): Flow<List<GlobeEntity>>

    fun getMomentsByGlobe(globeId: Long): Flow<PagingData<MomentWithGlobesAndPictures>>

    suspend fun getFirstMomentByGlobe(globeId: Long): MomentWithGlobesAndPictures?

    suspend fun getMomentCountByGlobe(globeId: Long): Int

    fun getMomentsNotInGlobe(globeId: Long): Flow<PagingData<MomentWithGlobesAndPictures>>
}