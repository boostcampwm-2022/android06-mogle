package com.wakeup.data.source.local.moment

import androidx.paging.PagingData
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.database.entity.PictureEntity
import kotlinx.coroutines.flow.Flow

interface MomentLocalDataSource {

    fun getMoments(query: String, sort: String): Flow<PagingData<MomentEntity>>

    suspend fun getPictures(momentId: Long): List<PictureEntity>

    suspend fun getGlobes(momentId: Long): List<GlobeEntity>

    suspend fun saveMoment(moment: MomentEntity): Long

    suspend fun savePicture(picture: List<PictureEntity>): List<Long>

    suspend fun saveMomentPicture(MomentPictures: List<MomentPictureEntity>)
}