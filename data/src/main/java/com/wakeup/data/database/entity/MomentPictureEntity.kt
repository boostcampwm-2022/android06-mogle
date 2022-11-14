package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moment_picture")
data class MomentPictureEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "moment_id") val momentId: Int,
    @ColumnInfo(name = "picture_id") val pictureId: Int,
)