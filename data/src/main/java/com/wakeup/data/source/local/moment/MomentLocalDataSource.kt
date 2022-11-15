package com.wakeup.data.source.local.moment

import com.wakeup.domain.model.Moment
import kotlinx.coroutines.flow.Flow

interface MomentLocalDataSource {
    fun getMoments(): Flow<List<Moment>>
}