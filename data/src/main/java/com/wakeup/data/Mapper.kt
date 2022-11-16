package com.wakeup.data

import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.data.model.LocationEntity
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment

fun MomentEntity.toDomain(pictures: List<PictureEntity>, globes: List<GlobeEntity>): Moment =
    Moment(
        this.location.mainAddress,
        this.location.detailAddress,
        pictures.map { it.bitmap },
        this.content,
        globes.map { it.name },
        this.date
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

fun String.toEntity(): PictureEntity =
    PictureEntity(
        bitmap = this
    )

fun toMomentPictureEntity(momentId: Long, pictureIds :List<Long>): List<MomentPictureEntity> =
    pictureIds.map { pictureId ->
        MomentPictureEntity(momentId = momentId, pictureId = pictureId)
    }
