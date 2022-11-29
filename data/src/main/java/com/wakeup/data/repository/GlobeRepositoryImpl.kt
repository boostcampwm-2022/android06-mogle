package com.wakeup.data.repository

import com.wakeup.data.database.mapper.toDomain
import com.wakeup.data.database.mapper.toEntity
import com.wakeup.data.source.local.globe.GlobeLocalDataSource
import com.wakeup.data.util.InternalFileUtil
import com.wakeup.domain.model.Globe
import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.GlobeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GlobeRepositoryImpl @Inject constructor(
    private val globeLocalDataSource: GlobeLocalDataSource,
    private val util: InternalFileUtil,
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

    override fun getMomentsByGlobe(globeId: Long): Flow<List<Moment>> {
        return globeLocalDataSource.getMomentsByGlobe(globeId).map {
            it.map { momentEntity ->
                momentEntity.toDomain(
                    util.getPictureInInternalStorage(
                        momentEntity.pictures,
                        momentEntity.moment.thumbnailId
                    )
                )
            }
        }
    }
}
