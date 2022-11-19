package com.wakeup.presentation.model

import android.graphics.Bitmap

data class MomentModel(
    val id: Long,
    val mainAddress: String,
    val detailAddress: String,
    val bitmapImages: List<Bitmap>?,
    val content: String,
    val globes: List<String>,
    val date: String
)
