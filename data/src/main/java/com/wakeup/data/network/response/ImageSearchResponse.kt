package com.wakeup.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ImageSearchResponse(
    val documents: List<ImageSearchResponseItem>,
)

@Serializable
data class ImageSearchResponseItem(
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    @SerialName("image_url") val imageUrl: String,
)