package com.wakeup.presentation.mapper

import com.wakeup.domain.model.Picture
import com.wakeup.presentation.model.PictureModel

fun PictureModel.toDomain(): Picture {
    return Picture(path = path)
}

fun Picture.toPresentation(): PictureModel {
    return PictureModel(path = path)
}
