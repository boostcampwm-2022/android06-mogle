package com.wakeup.domain.repository

import androidx.paging.PagingData
import com.wakeup.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MomentRepository {
    fun getMoments(
        sort: SortType,
        query: String,
        myLocation: Location? = null
    ): Flow<PagingData<Moment>>

    suspend fun saveMoment(moment: Moment, location: Place, pictures: List<Picture>?)
}