package com.wakeup.presentation.model

import android.os.Parcelable
import com.wakeup.presentation.util.DateUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class MomentModel(
    val id: Long = 0,
    val place: PlaceModel,
    val pictures: List<PictureModel>,
    val content: String,
    val globes: List<GlobeModel>,
    val date: Long,
) : Parcelable {

    fun getFormattedDate(): String = DateUtil.getFormattedDate(date)
}