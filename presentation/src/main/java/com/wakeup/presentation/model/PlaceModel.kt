package com.wakeup.presentation.model

import com.wakeup.domain.model.Place

data class PlaceModel(
    val mainAddress: String,
    val detailAddress: String
)

fun PlaceModel.toDomain(): Place {
    return Place(
        mainAddress = mainAddress,
        detailAddress = detailAddress
    )
}

fun Place.toPresentation(): PlaceModel {
    return PlaceModel(
        mainAddress = mainAddress,
        detailAddress = detailAddress
    )
}
//
//        mainAddress
//        detail
//        위도
//        경도
//        Location
//
//        Picture
//        Bitmap
//
//        Moment
//        Location
//        Date
//        content