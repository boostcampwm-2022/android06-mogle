package com.wakeup.domain.repository

import androidx.paging.PagingData
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface MomentRepository {
    fun getMoments(
        sort: SortType,
        query: String,
        myLocation: Location? = null
    ): Flow<PagingData<Moment>>

    fun getAllMoments(query: String): Flow<List<Moment>>

    suspend fun saveMoment(moment: Moment): Long

    suspend fun deleteMoment(momentId: Long)
}