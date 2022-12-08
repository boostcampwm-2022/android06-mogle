package com.wakeup.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wakeup.data.database.entity.PictureEntity

@Dao
interface PictureDao {
    @Query("SELECT picture_id FROM picture WHERE path = :path")
    suspend fun getPictureIdByByteArray(path: String): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun savePictures(pictures: List<PictureEntity>): List<Long>

    @Query("DELETE FROM picture WHERE picture_id = :pictureId")
    suspend fun deletePicture(pictureId: Long)
}