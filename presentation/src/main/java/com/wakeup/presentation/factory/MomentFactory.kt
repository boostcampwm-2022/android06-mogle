package com.wakeup.presentation.factory

import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.model.PlaceModel

object MomentFactory {

    private const val ONE_DAY = 86400000

    fun createMoments(count: Int): List<MomentModel> =
        (1..count).map {
            MomentModel(
                1L,
                createPlace(it),
                null,
                "내용${it}",
                listOf(GlobeModel("default")),
                System.currentTimeMillis() + (it * ONE_DAY)
            )
        }

    fun createPlace(count: Int): PlaceModel = PlaceModel(
        "메인 장소${count}",
        "상세 주소${count}",
        37.0 + count,
        128.0 + count
    )
}