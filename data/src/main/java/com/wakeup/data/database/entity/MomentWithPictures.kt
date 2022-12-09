package com.wakeup.data.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MomentWithPictures(
    @Embedded val moment: MomentEntity,
    @Relation(
        parentColumn = "moment_id",
        entity = PictureEntity::class,
        entityColumn = "picture_id",
        associateBy = Junction(MomentPictureXRef::class)
    )
    val pictures: List<PictureEntity>,
)
