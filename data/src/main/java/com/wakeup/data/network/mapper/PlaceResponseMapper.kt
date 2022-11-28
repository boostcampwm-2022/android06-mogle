package com.wakeup.data.network.mapper

import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.mapper.toDomain
import com.wakeup.data.network.response.PlaceResponseItem
import com.wakeup.domain.model.Place


fun PlaceResponseItem.toDomain(): Place {
    return Place(
        mainAddress = placeName,
        detailAddress = roadAddressName,
        placeUrl = placeUrl,
        location = LocationEntity(y.toDouble(), x.toDouble()).toDomain(),
    )
}