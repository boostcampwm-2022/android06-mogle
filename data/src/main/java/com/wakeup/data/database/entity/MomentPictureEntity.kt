package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "moment_picture",
    foreignKeys = [
        ForeignKey(
            entity = MomentEntity::class,
            parentColumns = ["moment_entity_id"],
            childColumns = ["moment_entity_id"],
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PictureEntity::class,
            parentColumns = ["picture_entity_id"],
            childColumns = ["picture_entity_id"],
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["moment_entity_id", "picture_entity_id"]
)
data class MomentPictureEntity(
    @ColumnInfo(name = "moment_entity_id", index = true) val momentId: Long,
    @ColumnInfo(name = "picture_entity_id", index = true) val pictureId: Long,
)