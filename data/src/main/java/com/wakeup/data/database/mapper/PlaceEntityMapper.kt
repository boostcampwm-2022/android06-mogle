package com.wakeup.data.database.mapper

import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.domain.model.Location
import com.wakeup.data.database.entity.PlaceEntity
import com.wakeup.domain.model.Place

fun PlaceEntity.toDomain(): Place {
    return Place(
        mainAddress = mainAddress,
        detailAddress = detailAddress,
        location = Location(latitude, longitude),
    )
}

fun Place.toEntity(): PlaceEntity {
    return PlaceEntity(
        mainAddress = mainAddress,
        detailAddress = detailAddress,
        latitude = location.latitude,
        longitude = location.longitude
    )
}

fun Location.toEntity(): LocationEntity {
    return LocationEntity(
        latitude = latitude,
        longitude = longitude,
    )
}

fun LocationEntity.toDomain(): Location {
    return Location(
        latitude = latitude,
        longitude = longitude,
    )
}