package com.wakeup.presentation.mapper

import com.wakeup.domain.model.Place
import com.wakeup.presentation.model.PlaceModel

fun PlaceModel.toDomain(): Place {
    return Place(
        mainAddress = mainAddress,
        detailAddress = detailAddress,
        location = location.toDomain(),
    )
}

fun Place.toPresentation(): PlaceModel {
    return PlaceModel(
        mainAddress = mainAddress,
        detailAddress = detailAddress,
        placeUrl = placeUrl ?: "",
        location = location.toPresentation(),
    )
}