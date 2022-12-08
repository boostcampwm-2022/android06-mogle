package com.wakeup.domain.model

data class Globe(
    val id: Long,
    val name: String,
    val momentCount: Int,
    val thumbnail: Picture?,
)