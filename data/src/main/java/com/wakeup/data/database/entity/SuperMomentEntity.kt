package com.wakeup.data.database.entity

data class SuperMomentEntity(
    val id: Long,
    val place: PlaceEntity,
    val pictures: List<PictureEntity>,
    val content: String,
    val globes: List<GlobeEntity>,
    val date: Long,
)