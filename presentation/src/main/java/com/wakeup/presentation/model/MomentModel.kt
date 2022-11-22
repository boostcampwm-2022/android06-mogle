package com.wakeup.presentation.model

import com.wakeup.presentation.util.DateUtil
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

    fun getFormattedDate(): String = DateUtil.getFormattedDate(date)
}