package com.wakeup.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GlobeModel(
    val id: Long = 0,
    val name: String,
    val momentCount: String = (-1).toString(),
) : Parcelable