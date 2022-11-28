package com.wakeup.data.database.dao

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wakeup.data.database.entity.GlobeEntity
import retrofit2.http.DELETE

interface GlobeDao {

    @Insert
    suspend fun createGlobe(globe: GlobeEntity)

    @Update
    suspend fun updateGlobe(globe: GlobeEntity)

    @DELETE
    suspend fun deleteGlobe(globe: GlobeEntity)

    @Query("SELECT * FROM globe")
    suspend fun getGlobes(): List<GlobeEntity>
}