package com.wakeup.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.PictureEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface MomentDao {
    @Query("SELECT * FROM moment")
    fun getMoments(): Flow<List<MomentEntity>>

    @Query(
        "SELECT * FROM picture WHERE id IN" +
                "(SELECT picture_id FROM moment_picture WHERE moment_id = :momentId)"
    )
    fun getPictures(momentId: Int): Flow<List<PictureEntity>>

    @Query(
        "SELECT * FROM globe WHERE id IN " +
                "(SELECT globe_id FROM moment_globe WHERE moment_id = :momentId)"
    )
    fun getGlobes(momentId: Int): Flow<List<GlobeEntity>>
}