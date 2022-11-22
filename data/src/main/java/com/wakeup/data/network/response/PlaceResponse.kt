package com.wakeup.data.network.response

import com.wakeup.data.database.mapper.toDomain
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.domain.model.Place
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
    val place_name: String,
    val road_address_name: String,
    val x: String,
    val y: String
)

fun PlaceResponseItem.toDomain(): Place {
    return Place(
        mainAddress = place_name,
        detailAddress = road_address_name,
        location = LocationEntity(y.toDouble(), x.toDouble()).toDomain()
    )
}


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