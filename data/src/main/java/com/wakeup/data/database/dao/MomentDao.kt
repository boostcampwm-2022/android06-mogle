package com.wakeup.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.PictureEntity
import kotlinx.coroutines.flow.Flow

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

    @Transaction
    @Query(
        """
        SELECT * FROM moment
        WHERE mainAddress LIKE '%' || :query || '%'
        OR detailAddress LIKE '%' || :query || '%'
        OR content LIKE '%' || :query || '%'
        """
    )
    fun getAllMoments(query: String): Flow<List<MomentWithGlobesAndPictures>>

    @Query("SELECT * FROM moment WHERE moment_id = :id")
    fun getMoment(id: Long): Flow<MomentWithGlobesAndPictures>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMoment(moment: MomentEntity): Long

    @Query(
        """
        SELECT picture.picture_id, picture.path
        FROM moment_picture 
        INNER JOIN picture 
        ON moment_picture.picture_id = picture.picture_id
        WHERE moment_picture.moment_id = :momentId
        """
    )
    suspend fun getMomentPictures(momentId: Long): List<PictureEntity>

    @Query("DELETE FROM moment WHERE moment_id = :momentId")
    suspend fun deleteMoment(momentId: Long)
}