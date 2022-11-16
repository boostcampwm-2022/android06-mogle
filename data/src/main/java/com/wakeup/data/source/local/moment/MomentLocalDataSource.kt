package com.wakeup.data.source.local.moment

import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.database.entity.PictureEntity
import kotlinx.coroutines.flow.Flow

interface MomentLocalDataSource {
    fun getMoments(): Flow<List<MomentEntity>>

    fun getPictures(momentId: Long): Flow<List<PictureEntity>>

    fun getGlobes(momentId: Long): Flow<List<GlobeEntity>>

    fun saveMoment(moment: MomentEntity): Long

    fun savePicture(picture: List<PictureEntity>): List<Long>

    fun saveMomentPicture(MomentPictures :List<MomentPictureEntity>)
}