package com.wakeup.data.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MomentWithGlobesAndPictures(
    @Embedded val moment: MomentEntity,
    @Relation(
        parentColumn = "moment_entity_id",
        entity = GlobeEntity::class,
        entityColumn = "globe_entity_id",
        associateBy = Junction(MomentGlobeEntity::class)
    )
    val globeList: List<GlobeEntity>,
    @Relation(
        parentColumn = "moment_entity_id",
        entity = PictureEntity::class,
        entityColumn = "picture_entity_id",
        associateBy = Junction(MomentPictureEntity::class)
    )
    val pictureList: List<PictureEntity>,
)