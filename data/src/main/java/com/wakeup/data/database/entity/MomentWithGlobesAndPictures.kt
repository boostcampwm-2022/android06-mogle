package com.wakeup.data.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MomentWithGlobesAndPictures(
    @Embedded val moment: MomentEntity,
    @Relation(
        parentColumn = "moment_id",
        entity = GlobeEntity::class,
        entityColumn = "globe_id",
        associateBy = Junction(MomentGlobeXRef::class)
    )
    val globes: List<GlobeEntity>,
    @Relation(
        parentColumn = "moment_id",
        entity = PictureEntity::class,
        entityColumn = "picture_id",
        associateBy = Junction(MomentPictureXRef::class)
    )
    val pictures: List<PictureEntity>,
)