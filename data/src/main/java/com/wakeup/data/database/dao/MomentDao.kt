package com.wakeup.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentGlobeEntity
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.PictureEntity

@Dao
interface MomentDao {

    @Transaction
    @Query(
        """
            SELECT * FROM moment
            WHERE mainAddress LIKE '%' || :query || '%'
            OR detailAddress LIKE '%' || :query || '%'
            OR content LIKE '%' || :query || '%'
            ORDER BY
            CASE WHEN :sortType = 0 THEN date END DESC,
            CASE WHEN :sortType = 1 THEN date END ASC
        """
    )
    fun getMoments(sortType: Int = 0, query: String): PagingSource<Int, MomentWithGlobesAndPictures>

    @Transaction
    @Query(
        """
            SELECT *, 
                (latitude - :lat) * (latitude - :lat) +
                (longitude - :lng) * (longitude - :lng) AS distance
            FROM moment
            WHERE mainAddress LIKE '%' || :query || '%' 
            OR detailAddress LIKE '%' || :query || '%'
            OR content LIKE '%' || :query || '%'
            ORDER BY distance
        """
    )
    fun getMomentsByNearestDistance(
        query: String,
        lat: Double?,
        lng: Double?,
    ): PagingSource<Int, MomentWithGlobesAndPictures>

    @Query("SELECT globe_entity_id FROM globe WHERE name = :globeName")
    suspend fun getGlobeId(globeName: String): Long

    @Query("SELECT picture_entity_id FROM picture WHERE bitmap = :bitmap")
    suspend fun getPictureByByteArray(bitmap: ByteArray): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMoment(moment: MomentEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun savePicture(picture: List<PictureEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMomentPicture(momentPictures: List<MomentPictureEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveGlobes(globes: List<GlobeEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMomentGlobe(momentGlobe: MomentGlobeEntity)
}