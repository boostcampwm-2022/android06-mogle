package com.wakeup.data

import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.data.model.LocationEntity
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.Picture

fun MomentEntity.toDomain(pictures: List<PictureEntity>, globes: List<GlobeEntity>): Moment =
    Moment(
        mainAddress = this.location.mainAddress,
        detailAddress = this.location.detailAddress,
        images = pictures.map { it.bitmap },
        content = this.content,
        globe = globes.map { it.name },
        date = this.date
    )

fun Moment.toEntity(location: Location, thumbnailId: Long): MomentEntity =
    MomentEntity(
        location = LocationEntity(
            mainAddress = location.mainAddress,
            detailAddress = location.detailAddress,
            latitude = location.latitude,
            longitude = location.longitude
        ),
        thumbnailId = thumbnailId,
        content = this.content,
        date = this.date
    )

fun Picture.toEntity(): PictureEntity =
    PictureEntity(
        bitmap = this.bitmap
    )

fun PictureEntity.toDomain(): Picture =
    Picture(
        bitmap = this.bitmap
    )