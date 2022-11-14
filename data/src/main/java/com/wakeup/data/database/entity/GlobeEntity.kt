package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "globe")
data class GlobeEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
)
