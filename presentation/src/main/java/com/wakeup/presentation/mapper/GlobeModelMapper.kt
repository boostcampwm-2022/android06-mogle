package com.wakeup.presentation.mapper

import com.wakeup.domain.model.Globe
import com.wakeup.presentation.model.GlobeModel

fun GlobeModel.toDomain(): Globe {
    return Globe(
        id = id,
        name = name
    )
}

fun Globe.toPresentation(): GlobeModel {
    return GlobeModel(
        id = id,
        name = name
    )
}
