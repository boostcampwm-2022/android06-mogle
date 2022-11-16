package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "moment_picture",
    foreignKeys = [
        ForeignKey(
            entity = MomentEntity::class,
            parentColumns = ["id"],
            childColumns = ["moment_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PictureEntity::class,
            parentColumns = ["id"],
            childColumns = ["picture_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MomentPictureEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "moment_id", index = true) val momentId: Int,
    @ColumnInfo(name = "picture_id", index = true) val pictureId: Int,
)