package com.wakeup.presentation.model

import android.graphics.Bitmap

data class MomentModel(
    val id: Int,
    val mainAddress: String,
    val detailAddress: String,
    val thumbnailBitmap: Bitmap?,
    val bitmapImages: List<Bitmap>?,
    val content: String,
    val globes: List<String>,
    val date: String
)
