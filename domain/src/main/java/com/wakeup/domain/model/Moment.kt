package com.wakeup.domain.model

data class Moment(
    val id: Long,
    val mainAddress: String,
    val detailAddress: String,
    val images: List<ByteArray>?,
    val content: String,
    val globes: List<String>,
    val date: String
)