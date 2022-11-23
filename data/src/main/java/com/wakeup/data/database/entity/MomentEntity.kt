package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "moment",
    foreignKeys = [
        ForeignKey(
            entity = PictureEntity::class,
            parentColumns = ["picture_id"],
            childColumns = ["thumbnail_id"],
            onUpdate = CASCADE
        )
    ]
)
data class MomentEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "moment_id") val id: Long = 0L,
    @Embedded val place: PlaceEntity,
    @ColumnInfo(name = "thumbnail_id", index = true) val thumbnailId: Long?,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "date") val date: Long,
)