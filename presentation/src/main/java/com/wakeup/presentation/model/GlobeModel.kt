package com.wakeup.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GlobeModel(
    val name: String,
) : Parcelable