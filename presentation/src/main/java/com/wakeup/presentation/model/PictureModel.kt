package com.wakeup.presentation.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PictureModel(
    val bitmap: Bitmap,
) : Parcelable