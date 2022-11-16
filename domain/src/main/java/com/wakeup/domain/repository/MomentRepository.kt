package com.wakeup.domain.repository

import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import kotlinx.coroutines.flow.Flow

interface MomentRepository {
    fun getMoments(): Flow<List<Moment>>

    fun saveMoment(moment: Moment, location: Location, pictures: List<String>)
}