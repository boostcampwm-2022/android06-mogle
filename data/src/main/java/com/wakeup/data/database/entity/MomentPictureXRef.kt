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
    ]
)
data class MomentPictureXRef(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "moment_id", index = true) val momentId: Long,
    @ColumnInfo(name = "picture_id", index = true) val pictureId: Long,
)