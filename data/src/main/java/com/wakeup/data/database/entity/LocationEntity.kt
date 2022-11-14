package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "main_address") val mainAddress: String,
    @ColumnInfo(name = "detail_address") val detailAddress: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
)
