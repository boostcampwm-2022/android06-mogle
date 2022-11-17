package com.wakeup.domain.repository

import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.Picture
import kotlinx.coroutines.flow.Flow

interface MomentRepository {
    fun getMoments(): Flow<List<Moment>>

    suspend fun saveMoment(moment: Moment, location: Location, pictures: List<Picture>)
}