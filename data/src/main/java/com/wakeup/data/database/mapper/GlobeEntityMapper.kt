package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.domain.model.Globe

fun Globe.toEntity(): GlobeEntity {
    return GlobeEntity(
        id = id,
        name = name,
        thumbnail = thumbnail?.toEntity(),
    )
}

fun GlobeEntity.toDomain(): Globe {
    return Globe(
        id = id,
        name = name,
        momentCount = 0,
        thumbnail = thumbnail?.toDomain(),
    )
}