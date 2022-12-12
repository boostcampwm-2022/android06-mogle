package com.wakeup.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef

@Dao
interface XRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMomentPictureXRefs(momentPictures: List<MomentPictureXRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMomentGlobeXRef(momentGlobe: MomentGlobeXRef)

    @Delete
    suspend fun deleteMomentGlobeXRef(momentGlobe: MomentGlobeXRef)

    @Query(
        """
        SELECT (CASE WHEN count(*) > 1 THEN 0 ELSE 1 END) FROM moment_picture
        WHERE picture_id = :pictureId
        """
    )
    suspend fun isOnlyOnePicture(pictureId: Long): Boolean

    // only use test
    @Query("""SELECT * FROM moment_picture""")
    suspend fun getAllMomentPictureXRef(): List<MomentPictureXRef>

    @Query("""SELECT picture_id FROM moment_picture WHERE moment_id=:momentId""")
    suspend fun getPictureIdsByMomentId(momentId: Long): List<Long>
}