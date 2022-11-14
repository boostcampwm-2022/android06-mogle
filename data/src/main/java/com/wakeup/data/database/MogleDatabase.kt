package com.wakeup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.MomentEntity

@Database(
    entities = [
        MomentEntity::class
    ],
    version = 1
)
abstract class MogleDatabase: RoomDatabase() {
    abstract fun momentDao(): MomentDao
}