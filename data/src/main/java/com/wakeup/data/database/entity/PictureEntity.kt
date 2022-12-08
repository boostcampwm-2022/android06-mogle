package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "picture",
    indices = [Index(value = ["path"], unique = true)]
)
data class PictureEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "picture_id") val id: Long = 0L,
    @ColumnInfo(name = "path") val path: String,
)