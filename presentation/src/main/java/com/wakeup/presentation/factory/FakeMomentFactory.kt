package com.wakeup.presentation.factory

import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.model.PictureModel
import com.wakeup.presentation.model.PlaceModel

object FakeMomentFactory {

    private const val ONE_DAY = 86400000

    fun createMoments(count: Int): List<MomentModel> =
        (1..count).map {
            MomentModel(
                id = 1L,
                place = createPlace(it),
                pictures = emptyList(),
                content = "재민${it}",
                globes = listOf(
                    GlobeModel(id = 0, name = "여행", thumbnail = null),
                    GlobeModel(id = 0, name = "default", thumbnail = null)
                ),
                date = System.currentTimeMillis() + (it * ONE_DAY)
            )
        }

    private fun createPlace(count: Int): PlaceModel = PlaceModel(
        mainAddress = "메인 장소${count}",
        detailAddress = "상세 주소${count}",
        placeUrl = "",
        location = LocationModel(37.0 + count, 128.0 + count),
    )

    fun createMomentsWithSampleImage(picture: PictureModel, count: Int): List<MomentModel> =
        (1..count).map {
            MomentModel(
                id = 1L,
                place = createPlace(it),
                pictures = listOf(picture, picture, picture, picture),
                content = "재민${it}",
                globes = listOf(
                    GlobeModel(id = 0, name = "여행", thumbnail = null),
                    GlobeModel(id = 1, name = "default", thumbnail = null)
                ),
                date = System.currentTimeMillis() + (it * ONE_DAY)
            )
        }

}