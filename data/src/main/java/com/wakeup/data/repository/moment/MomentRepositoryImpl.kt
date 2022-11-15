package com.wakeup.data.repository.moment

import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.data.source.toDomain
import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.last

class MomentRepositoryImpl(
    private val localDataSource: MomentLocalDataSource
): MomentRepository {

    override fun getMoments(): Flow<List<Moment>> =
        localDataSource.getMoments().map { moments ->
            moments.map { momentEntity ->
                momentEntity.toDomain(
                    localDataSource.getLocation(momentEntity.locationId).last(),
                    localDataSource.getPictures(momentEntity.id).last(),
                    localDataSource.getGlobes(momentEntity.id).last()
                )
            }
        }
}