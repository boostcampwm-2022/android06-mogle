package com.wakeup.domain.model

data class Place(
    val mainAddress: String,
    val detailAddress: String,
    val placeUrl: String? = null,
    val placeImageUrl: String? = null,
    val location: Location,
)