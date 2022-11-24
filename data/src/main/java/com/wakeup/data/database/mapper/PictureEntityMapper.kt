package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.Picture

fun Picture.toEntity(fileName: String): PictureEntity {
    return PictureEntity(
        fileName = fileName,
    )
}

fun PictureEntity.toDomain(bitmap: ByteArray): Picture {
    return Picture(
        bitmap = bitmap,
    )
}