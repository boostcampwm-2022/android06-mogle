package com.wakeup.data.repository.moment

import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow

class MomentRepositoryImpl(
    private val localDataSource: MomentLocalDataSource
): MomentRepository {

    override fun getMoments(): Flow<List<Moment>> = localDataSource.getMoments()

}