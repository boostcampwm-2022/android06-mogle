package com.wakeup.data.source.local.moment

import androidx.paging.PagingData
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.SuperMomentEntity
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface MomentLocalDataSource {

    fun getMoments(
        sortType: SortType,
        query: String,
        myLocation: LocationEntity?
    ): Flow<PagingData<MomentWithGlobesAndPictures>>

    fun getAllMoments(query: String): Flow<List<MomentWithGlobesAndPictures>>

    fun getMoment(momentId: Long): Flow<MomentWithGlobesAndPictures>

    suspend fun saveMoment(moment: SuperMomentEntity, id: Long? = null)

    suspend fun deleteMoment(momentId: Long)

    suspend fun updateMoment(moment: SuperMomentEntity)
}