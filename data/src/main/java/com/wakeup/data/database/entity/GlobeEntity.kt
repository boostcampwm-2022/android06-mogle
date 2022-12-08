package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "globe",
    indices = [Index(value = ["name"], unique = true)]
)
data class GlobeEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "globe_id") val id: Long = 0L,
    @ColumnInfo(name = "name") val name: String,
    @Embedded val thumbnail: PictureEntity?
)