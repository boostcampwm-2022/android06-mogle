package com.wakeup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentGlobeEntity
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.database.entity.PictureEntity

@Database(
    entities = [
        MomentEntity::class,
        GlobeEntity::class,
        MomentGlobeEntity::class,
        MomentPictureEntity::class,
        PictureEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MogleDatabase: RoomDatabase() {
    abstract fun momentDao(): MomentDao
}