package com.wakeup.data.model

data class LocationEntity(
    val id: Int = 0,
    val mainAddress: String,
    val detailAddress: String,
    val latitude: Double,
    val longitude: Double,
)
