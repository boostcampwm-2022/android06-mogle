package com.wakeup.data.source.local.moment

import androidx.paging.PagingData
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface MomentLocalDataSource {

    fun getMoments(
        sortType: SortType,
        query: String,
        myLocation: LocationEntity?
    ): Flow<PagingData<MomentWithGlobesAndPictures>>

    fun getAllMoments(): Flow<List<MomentWithGlobesAndPictures>>

/*    suspend fun getPictures(momentId: Long): List<PictureEntity>

    suspend fun getGlobes(momentId: Long): List<GlobeEntity>*/

    suspend fun saveMoment(moment: MomentEntity): Long

    suspend fun savePictures(pictures: List<PictureEntity>): List<Long>
}