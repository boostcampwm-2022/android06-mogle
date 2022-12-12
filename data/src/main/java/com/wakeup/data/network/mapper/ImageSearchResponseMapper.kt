package com.wakeup.data.network.mapper

import com.wakeup.data.network.response.ImageSearchResponseItem
import com.wakeup.domain.model.Picture

fun ImageSearchResponseItem.toDomain(): Picture {
    return Picture(
        path = thumbnailUrl,
    )
}