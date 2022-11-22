package com.wakeup.domain.repository

import androidx.paging.PagingData
import com.wakeup.domain.model.Picture
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.Place
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface MomentRepository {
    fun getMoments(
        sort: SortType,
        query: String,
        myLocation: Location? = null
    ): Flow<PagingData<Moment>>

    suspend fun saveMoment(moment: Moment)
}