package com.wakeup.domain.model

data class Moment(
    val id: Long,
    val place: Place,
    val pictures: List<Picture>?,
    val content: String,
    val globes: List<Globe>,
    val date: Long,
)