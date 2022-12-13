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

    @Transaction
    @Query("""
        SELECT m.moment_id, m.content, m.date, m.mainAddress, m.detailAddress, m.latitude, m.longitude 
        FROM moment_globe AS mg JOIN moment AS m ON mg.moment_id = m.moment_id WHERE mg.globe_id = :globeId
    """)
    fun getMomentsByGlobe(globeId: Long): PagingSource<Int, MomentWithGlobesAndPictures>

    @Transaction
    @Query("""
        SELECT m.moment_id, m.content, m.date, m.mainAddress, m.detailAddress, m.latitude, m.longitude 
        FROM moment_globe AS mg JOIN moment AS m ON mg.moment_id = m.moment_id WHERE mg.globe_id = :globeId LIMIT 1
    """)
    suspend fun getFirstMomentByGlobe(globeId: Long): MomentWithGlobesAndPictures?

    @Query("SELECT COUNT(moment_id) FROM moment_globe WHERE globe_id = :globeId")
    suspend fun getMomentCountByGlobe(globeId: Long): Int

    @Query("SELECT * FROM moment WHERE moment_id NOT IN (SELECT moment_id FROM moment_globe WHERE globe_id = :globeId)")
    fun getMomentsNotInGlobe(globeId: Long): PagingSource<Int, MomentWithGlobesAndPictures>
}