package com.wakeup.presentation.model

import java.text.SimpleDateFormat
import java.util.*

data class MomentModel(
    val id: Long,
    val place: PlaceModel,
    val pictures: List<PictureModel>?,
    val content: String,
    val globes: List<GlobeModel>,
    val date: Long,
) {
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun getFormattedDate(): String = sdf.format(date)
}