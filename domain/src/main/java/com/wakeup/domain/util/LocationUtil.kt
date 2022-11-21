package com.wakeup.domain.util

import com.wakeup.domain.model.Location
import kotlin.math.pow
import kotlin.math.sqrt

object LocationUtil {

    fun Location.distanceTo(other: Location): Double =
        sqrt((latitude - other.latitude).pow(2) + (longitude - other.longitude).pow(2))
}