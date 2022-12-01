package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.Picture

fun MomentWithGlobesAndPictures.toDomain(pictures: List<Picture>): Moment {
    return Moment(
        id = moment.id,
        place = moment.place.toDomain(),
        pictures = pictures,
        content = moment.content,
        globes = globes.map { it.toDomain() },
        date = moment.date,
    )
}


fun Moment.toEntity(thumbnailId: Long? = null): MomentEntity {
    return MomentEntity(
        place = place.toEntity(),
        thumbnailId = thumbnailId,
        content = content,
        date = date,
    )
}