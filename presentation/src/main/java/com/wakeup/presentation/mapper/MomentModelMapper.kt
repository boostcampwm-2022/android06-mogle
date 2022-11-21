package com.wakeup.presentation.mapper

import com.wakeup.domain.model.Moment
import com.wakeup.presentation.model.MomentModel

fun MomentModel.toDomain(): Moment {
    return Moment(
        id = id,
        content = content,
        place = place.toDomain(),
        pictures = pictures?.map { it.toDomain() },
        date = date,
        globes = globes.map { it.toDomain() }
    )
}

fun Moment.toPresentation(): MomentModel {
    return MomentModel(
        id = id,
        content = content,
        place = place.toPresentation(),
        pictures = pictures?.map { it.toPresentation() },
        date = date,
        globes = globes.map { it.toPresentation() }
    )
}
