package com.wakeup.data.network.mapper

import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.mapper.toDomain
import com.wakeup.data.network.response.PlaceResponseItem
import com.wakeup.domain.model.Place


fun PlaceResponseItem.toDomain(): Place {
    return Place(
        mainAddress = place_name,
        detailAddress = road_address_name,
        placeUrl = place_url,
        location = LocationEntity(y.toDouble(), x.toDouble()).toDomain(),
    )
}