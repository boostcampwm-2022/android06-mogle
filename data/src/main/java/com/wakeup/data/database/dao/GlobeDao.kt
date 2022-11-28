package com.wakeup.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wakeup.data.database.entity.GlobeEntity
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
}