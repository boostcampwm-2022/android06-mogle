package com.wakeup.data

import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.Moment

fun MomentEntity.toDomain(
    pictures: List<PictureEntity>,
    globes: List<GlobeEntity>
): Moment =
    Moment(
        this.location.mainAddress,
        this.location.detailAddress,
        pictures.map { it.bitmap },
        this.content,
        globes.map { it.name },
        this.date
    )