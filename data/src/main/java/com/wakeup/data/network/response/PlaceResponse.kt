package com.wakeup.data.network.response

import com.wakeup.domain.model.Place
import kotlinx.serialization.Serializable

@Serializable
data class PlaceResponse(
    val documents: List<PlaceResponseItem>,
    val meta: Meta
)

@Serializable
data class PlaceResponseItem(
    val address_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val category_name: String,
    val distance: String,
    val id: String,
    val phone: String,
    val place_name: String,
    val place_url: String,
    val road_address_name: String,
    val x: String,
    val y: String
)

fun PlaceResponseItem.toDomain(): Place {
    return Place(
        mainAddress = place_name,
        detailAddress = road_address_name,
        latitude = y.toDouble(),
        longitude = x.toDouble()
    )
}


@Serializable
data class Meta(
    val is_end: Boolean,
    val pageable_count: Int,
    val same_name: SameName,
    val total_count: Int
)

@Serializable
data class SameName(
    val keyword: String,
    val region: List<String>,
    val selected_region: String
)