package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.domain.model.Globe

fun Globe.toEntity(): GlobeEntity {
    return GlobeEntity(
        name = name,
    )
}

fun GlobeEntity.toDomain(): Globe {
    return Globe(
        name = name,
    )
}