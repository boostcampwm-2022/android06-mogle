package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moment")
data class MomentEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "moment_id") val id: Long = 0L,
    @Embedded val place: PlaceEntity,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "date") val date: Long,
)