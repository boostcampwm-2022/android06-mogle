package com.wakeup.data.source.local.moment

import androidx.paging.PagingData
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface MomentLocalDataSource {

    fun getMoments(
        sortType: SortType,
        query: String,
        myLocation: LocationEntity?
    ): Flow<PagingData<MomentWithGlobesAndPictures>>

    fun getAllMoments(query: String): Flow<List<MomentWithGlobesAndPictures>>

    suspend fun saveMoment(moment: MomentEntity): Long

    suspend fun deleteMoment(momentId: Long)
}