package com.wakeup.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceResponse(
    val documents: List<PlaceResponseItem>,
//    val meta: Meta
)

/**
 * address_name: String,
 * category_group_code: String,
 * category_group_name: String,
 * category_name: String,
 * distance: String,
 * id: String,
 * phone: String,
 * place_name: String,
 * place_url: String,
 * road_address_name: String,
 * x: String,
 * y: String
 */
@Serializable
data class PlaceResponseItem(
    @SerialName("place_name") val placeName: String,
    @SerialName("road_address_name") val roadAddressName: String,
    @SerialName("place_url") val placeUrl: String,
    @SerialName("x") val x: String,
    @SerialName("y") val y: String
)


//@Serializable
//data class Meta(
//    val is_end: Boolean,
//    val pageable_count: Int,
//    val same_name: SameName,
//    val total_count: Int
//)

//@Serializable
//data class SameName(
//    val keyword: String,
//    val region: List<String>,
//    val selected_region: String
//)