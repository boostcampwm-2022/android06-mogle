package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.PlaceEntity
import com.wakeup.domain.model.Place

fun PlaceEntity.toDomain(): Place {
    return Place(
        mainAddress = mainAddress,
        detailAddress = detailAddress,
        latitude = latitude,
        longitude = longitude,
    )
}

fun Place.toEntity(): PlaceEntity {
    return PlaceEntity(
        mainAddress = mainAddress,
        detailAddress = detailAddress,
        latitude = latitude,
        longitude = longitude,
    )
}