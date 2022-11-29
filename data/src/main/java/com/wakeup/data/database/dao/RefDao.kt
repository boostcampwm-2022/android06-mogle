package com.wakeup.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef

// TODO: Ref 로 할 지 XRef 로 할 지 결정 후 수정

@Dao
interface RefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMomentPictureRefs(momentPictures: List<MomentPictureXRef>)

    @Insert
    suspend fun saveMomentGlobeRef(momentGlobe: MomentGlobeXRef)

    @Delete
    suspend fun deleteMomentGlobeRef(momentGlobe: MomentGlobeXRef)
}