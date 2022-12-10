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
    var isSelected: Boolean = false // 동시 접근할 일 없으므로 var 선언
) : Parcelable {

    fun getFormattedDate(): String = DateUtil.getFormattedDate(date)
}