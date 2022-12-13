package com.wakeup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wakeup.data.database.dao.GlobeDao
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.dao.PictureDao
import com.wakeup.data.database.dao.XRefDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
import com.wakeup.data.database.entity.PictureEntity
import java.util.concurrent.*

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
    abstract fun pictureDao(): PictureDao
    abstract fun globeDao(): GlobeDao
    abstract fun xRefDao(): XRefDao


    companion object {
        val callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadScheduledExecutor().execute {
                    db.execSQL("INSERT INTO globe (name) VALUES ('기본')")
                }
            }
        }
    }
}