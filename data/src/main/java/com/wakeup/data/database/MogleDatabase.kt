package com.wakeup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wakeup.data.database.dao.GlobeDao
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.dao.RefDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
import com.wakeup.data.database.entity.PictureEntity
import java.util.concurrent.Executors

@Database(
    entities = [
        MomentEntity::class,
        GlobeEntity::class,
        MomentGlobeXRef::class,
        MomentPictureXRef::class,
        PictureEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MogleDatabase : RoomDatabase() {
    abstract fun momentDao(): MomentDao
    abstract fun globeDao(): GlobeDao
    abstract fun refDao(): RefDao

    companion object {
        val callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadScheduledExecutor().execute {
                    db.execSQL("INSERT INTO globe (name) VALUES ('Default')")
                }
            }
        }
    }
}