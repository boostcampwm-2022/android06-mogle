package com.wakeup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.*

@Database(
    entities = [
        MomentEntity::class,
        GlobeEntity::class,
        LocationEntity::class,
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