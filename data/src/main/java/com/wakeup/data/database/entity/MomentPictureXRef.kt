package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "moment_picture",
    foreignKeys = [
        ForeignKey(
            entity = MomentEntity::class,
            parentColumns = ["moment_id"],
            childColumns = ["moment_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = PictureEntity::class,
            parentColumns = ["picture_id"],
            childColumns = ["picture_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ],
    primaryKeys = ["moment_id", "picture_id"]
)
data class MomentPictureXRef(
    @ColumnInfo(name = "moment_id", index = true) val momentId: Long,
    @ColumnInfo(name = "picture_id", index = true) val pictureId: Long,
)