package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moment_globe")
data class MomentGlobeEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "moment_id") val momentId: Int,
    @ColumnInfo(name = "globe_id") val globeId: Int,
)
