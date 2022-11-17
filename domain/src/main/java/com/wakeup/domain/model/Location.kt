package com.wakeup.domain.model

data class Location(
    val mainAddress: String,
    val detailAddress: String,
    val latitude: Double,
    val longitude: Double
)