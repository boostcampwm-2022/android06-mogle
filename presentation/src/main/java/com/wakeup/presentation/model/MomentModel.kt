package com.wakeup.presentation.model

data class MomentModel(
    val id: Long = 0,
    val place: PlaceModel,
    val pictures: List<PictureModel>?,
    val content: String,
    val globes: List<GlobeModel>,
    val date: String,
)
