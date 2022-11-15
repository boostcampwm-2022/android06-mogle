package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "moment",
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["location_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = PictureEntity::class,
            parentColumns = ["id"],
            childColumns = ["thumbnail_id"],
            onDelete = CASCADE
        )
    ]
)
data class MomentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "location_id", index = true) val locationId: Int,
    @ColumnInfo(name = "thumbnail_id", index = true) val thumbnailId: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "date") val date: String,
)