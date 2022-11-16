package com.wakeup.presentation.model

data class MomentModel(
    val id: Int,
    val mainAddress: String,
    val detailAddress: String,
    val bitmapImages: List<String>,
    val content: String,
    val globes: List<String>,
    val date: String
)
