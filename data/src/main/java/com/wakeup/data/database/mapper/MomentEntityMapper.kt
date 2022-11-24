package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.Picture
import com.wakeup.domain.model.Place

fun MomentWithGlobesAndPictures.toDomain(pictures: List<Picture>, globes: List<GlobeEntity>): Moment {
    return Moment(
        id = moment.id,
        place = moment.place.toDomain(),
        pictures = pictures,
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