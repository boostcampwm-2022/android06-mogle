package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moment")
data class MomentEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "thumbnail_id") val thumbnailId: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "date") val date: String,
)