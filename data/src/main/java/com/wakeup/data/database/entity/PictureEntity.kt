package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "picture",
    indices = [Index(value = ["fileName"], unique = true)]
)
data class PictureEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "picture_id") val id: Long = 0L,
    @ColumnInfo(name = "fileName") val fileName: String,
)