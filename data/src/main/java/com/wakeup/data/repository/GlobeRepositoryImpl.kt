package com.wakeup.data.repository

import com.wakeup.data.database.mapper.toDomain
import com.wakeup.data.database.mapper.toEntity
import com.wakeup.data.source.local.globe.GlobeLocalDataSource
import com.wakeup.domain.model.Globe
import com.wakeup.domain.repository.GlobeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GlobeRepositoryImpl @Inject constructor(
    private val localDataSource: GlobeLocalDataSource
) : GlobeRepository {

    override suspend fun createGlobe(globe: Globe) {
        localDataSource.createGlobe(globe.toEntity())
    }

    override suspend fun updateGlobe(globe: Globe) {
        localDataSource.updateGlobe(globe.toEntity())
    }

    override suspend fun deleteGlobe(globe: Globe) {
        localDataSource.deleteGlobe(globe.toEntity())
    }

    override fun getGlobes(): Flow<List<Globe>> {
        return localDataSource.getGlobes().map {
            it.map { globeEntity -> globeEntity.toDomain() }
        }
    }
}
