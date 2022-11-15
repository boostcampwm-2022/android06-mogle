package com.wakeup.data.source

import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.Moment

fun MomentEntity.toDomain(
    location: LocationEntity, pictures: List<PictureEntity>, globes: List<GlobeEntity>
): Moment = Moment(
    location.mainAddress,
    location.detailAddress,
    pictures.map { it.bitmap },
    this.content,
    globes.map { it.name },
    this.date
)