package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.Place

fun MomentEntity.toDomain(pictures: List<PictureEntity>, globes: List<GlobeEntity>): Moment {
    return Moment(
        id = id,
        place = place.toDomain(),
        pictures = pictures.map { it.toDomain() },
        content = content,
        globes = globes.map { it.toDomain() },
        date = date
    )
}

fun MomentWithGlobesAndPictures.toDomain(pictures: List<PictureEntity>, globes: List<GlobeEntity>): Moment {
    return Moment(
        id = moment.id,
        place = moment.place.toDomain(),
        pictures = pictures.map { it.toDomain() },
        content = moment.content,
        globes = globes.map { it.toDomain() },
        date = moment.date
    )
}


fun Moment.toEntity(place: Place, thumbnailId: Long?): MomentEntity {
    return MomentEntity(
        place = place.toEntity(),
        thumbnailId = thumbnailId,
        content = content,
        date = date,
    )
}