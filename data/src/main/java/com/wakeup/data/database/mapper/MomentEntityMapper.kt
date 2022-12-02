package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.domain.model.Moment

fun MomentWithGlobesAndPictures.toDomain(): Moment {
    return Moment(
        id = moment.id,
        place = moment.place.toDomain(),
        pictures = pictures.map { it.toDomain() },
        content = moment.content,
        globes = globes.map { it.toDomain() },
        date = moment.date,
    )
}


fun Moment.toEntity(): MomentEntity {
    return MomentEntity(
        place = place.toEntity(),
        content = content,
        date = date,
    )
}