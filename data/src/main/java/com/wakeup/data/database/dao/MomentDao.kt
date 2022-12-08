package com.wakeup.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
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
    suspend fun getMoment(id: Long): MomentWithGlobesAndPictures

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMoment(moment: MomentEntity): Long
}