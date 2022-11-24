package com.wakeup.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
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

    @Query("SELECT globe_id FROM globe WHERE name = :globeName")
    suspend fun getGlobeIdByName(globeName: String): Long

    @Query("SELECT picture_id FROM picture WHERE fileName = :fileName")
    suspend fun getPictureIdByByteArray(fileName: String): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMoment(moment: MomentEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun savePictures(pictures: List<PictureEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMomentPictureXRefs(momentPictures: List<MomentPictureXRef>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveGlobes(globes: List<GlobeEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMomentGlobeXRef(momentGlobe: MomentGlobeXRef)
}