package com.wakeup.domain.model

data class Weather(
    val id: Long,
    val type: String,
    val iconUrl: String,
    val temperature: Double,
)
