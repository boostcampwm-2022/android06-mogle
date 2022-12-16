package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.SuperMomentEntity
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

fun Moment.toEntity(): SuperMomentEntity {
    return SuperMomentEntity(
        id = id,
        place = place.toEntity(),
        pictures = pictures.map { picture -> picture.toEntity() },
        content = content,
        globes = globes.map { globe -> globe.toEntity() },
        date = date
    )
}

fun SuperMomentEntity.toMomentEntity() : MomentEntity {
    return MomentEntity(
        place = place,
        content = content,
        date = date,
    )
}

fun SuperMomentEntity.toMomentEntity(id: Long) : MomentEntity {
    return MomentEntity(
        id = id,
        place = place,
        content = content,
        date = date,
    )
}