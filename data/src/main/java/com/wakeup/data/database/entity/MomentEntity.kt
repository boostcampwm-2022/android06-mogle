package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.wakeup.data.model.LocationEntity

@Entity(
    tableName = "moment",
    foreignKeys = [
        ForeignKey(
            entity = PictureEntity::class,
            parentColumns = ["id"],
            childColumns = ["thumbnail_id"],
            onDelete = CASCADE
        )
    ]
)
data class MomentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @Embedded val location: LocationEntity,
    @ColumnInfo(name = "thumbnail_id") val thumbnailId: Long,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "date") val date: String,
)