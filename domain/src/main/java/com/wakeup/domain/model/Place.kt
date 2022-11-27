package com.wakeup.domain.model

data class Place(
    val mainAddress: String,
    val detailAddress: String,
    val imageUrl: String? = null,
    val location: Location,
)