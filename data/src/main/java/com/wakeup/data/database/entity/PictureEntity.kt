package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture")
data class PictureEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "bitmap") val bitmap: String,
)