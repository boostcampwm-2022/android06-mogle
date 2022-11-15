package com.wakeup.domain.model

data class Moment(
    val mainAddress: String,
    val detailAddress: String,
    val images: List<String>,
    val content: String,
    val globe: List<String>,
    val date: String
)