package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.Picture

fun Picture.toEntity(): PictureEntity {
    return PictureEntity(
        path = path
    )
}

fun PictureEntity.toDomain(): Picture {
    return Picture(
        path = path,
    )
}