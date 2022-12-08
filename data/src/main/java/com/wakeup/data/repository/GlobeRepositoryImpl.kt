package com.wakeup.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.wakeup.data.database.mapper.toDomain
import com.wakeup.data.database.mapper.toEntity
import com.wakeup.data.source.local.globe.GlobeLocalDataSource
import com.wakeup.domain.model.Globe
import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.GlobeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GlobeRepositoryImpl @Inject constructor(
    private val globeLocalDataSource: GlobeLocalDataSource,
) : GlobeRepository {

    override suspend fun createGlobe(globe: Globe) {
        globeLocalDataSource.createGlobe(globe.toEntity())
    }

    override suspend fun updateGlobe(globe: Globe) {
        globeLocalDataSource.updateGlobe(globe.toEntity())
    }

    override suspend fun deleteGlobe(globe: Globe) {
        globeLocalDataSource.deleteGlobe(globe.toEntity())
    }

    override fun getGlobes(): Flow<List<Globe>> {
        return globeLocalDataSource.getGlobes().map {
            it.map { globeEntity -> globeEntity.toDomain() }
        }
    }

    override fun getMomentsByGlobe(globeId: Long): Flow<PagingData<Moment>> =
        globeLocalDataSource.getMomentsByGlobe(globeId).map { pagingData ->
            pagingData.map { momentEntity -> momentEntity.toDomain() }
        }

    override suspend fun getFirstMomentByGlobe(globeId: Long): Moment? {
        return globeLocalDataSource.getFirstMomentByGlobe(globeId)?.toDomain()
    }

    override suspend fun getMomentCountByGlobe(globeId: Long): Int {
        return globeLocalDataSource.getMomentCountByGlobe(globeId)
    }

    override fun getMomentsNotInGlobe(globeId: Long): Flow<PagingData<Moment>> {
        return globeLocalDataSource.getMomentsNotInGlobe(globeId).map { pagingData ->
            pagingData.map { momentXRefs -> momentXRefs.toDomain() }
        }
    }
}
