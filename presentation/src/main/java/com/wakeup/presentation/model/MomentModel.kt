package com.wakeup.presentation.model

import com.wakeup.presentation.util.DateUtil
import java.io.Serializable

// TODO @Parcelize 변경
data class MomentModel(
    val id: Long = 0,
    val place: PlaceModel,
    val pictures: List<PictureModel>,
    val content: String,
    val globes: List<GlobeModel>,
    val date: Long,
): Serializable {

    fun getFormattedDate(): String = DateUtil.getFormattedDate(date)
}