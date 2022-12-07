package com.wakeup.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import kotlinx.coroutines.flow.Flow

@Dao
interface GlobeDao {

    @Insert
    suspend fun createGlobe(globe: GlobeEntity)

    @Update
    suspend fun updateGlobe(globe: GlobeEntity)

    @Delete
    suspend fun deleteGlobe(globe: GlobeEntity)

    @Query("SELECT * FROM globe")
    fun getGlobes(): Flow<List<GlobeEntity>>

    @Query("SELECT globe_id FROM globe WHERE name = :name")
    suspend fun getGlobeIdByName(name: String): Long

    @Transaction
    @Query("SELECT * FROM moment WHERE moment_id IN (SELECT moment_id FROM moment_globe WHERE globe_id = :globeId)")
    fun getMomentsByGlobe(globeId: Long): PagingSource<Int, MomentWithGlobesAndPictures>

    @Query("SELECT COUNT(moment_id) FROM moment_globe WHERE globe_id = :globeId")
    suspend fun getMomentCountByGlobe(globeId: Long): Int
}