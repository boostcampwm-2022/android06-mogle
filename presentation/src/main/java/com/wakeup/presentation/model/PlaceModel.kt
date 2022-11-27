package com.wakeup.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceModel(
    val mainAddress: String,
    val detailAddress: String,
    val placeUrl: String,
    val location: LocationModel,
) : Parcelable