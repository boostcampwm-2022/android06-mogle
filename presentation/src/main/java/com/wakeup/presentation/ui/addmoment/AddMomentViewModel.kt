package com.wakeup.presentation.ui.addmoment

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.SaveMomentUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.model.PictureModel
import com.wakeup.presentation.model.PlaceModel
import com.wakeup.presentation.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AddMomentViewModel @Inject constructor(
    private val saveMomentUseCase: SaveMomentUseCase
) : ViewModel() {

    // mock data
    private val globes = listOf(
        GlobeModel(0, "globe 1"),
        GlobeModel(1, "globe 2"),
        GlobeModel(2, "globe 3"),
    )

    val defaultGlobe = globes.first()

    // mock data
    val tmpGlobes = arrayOf(
        ("globe 1"),
        ("globe 2"),
        ("globe 3"),
    )

    private val _pictures = MutableStateFlow<List<PictureModel>>(emptyList())
    val pictures = _pictures.asStateFlow()

    private val _isPictureMax = MutableStateFlow(false)
    val isPictureMax = _isPictureMax.asStateFlow()

    private var selectedGlobe = MutableStateFlow(globes.first())

    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())

    val selectedDateByTime = _selectedDate.map { date ->
        DateUtil.getDateByTime(date)
    }.asLiveData()

    // initial data
    private val _place: MutableStateFlow<PlaceModel> = MutableStateFlow(
        PlaceModel(
            "",
            "",
            "",
            LocationModel(0.0, 0.0)
        )
    )
    val place = _place.asStateFlow()

    val content = MutableStateFlow("")

    private val _isSaveButtonEnabled = MutableStateFlow(false)
    val isSaveButtonEnabled = _isSaveButtonEnabled.asStateFlow()

    private val _isSaveButtonClicked = MutableStateFlow(false)
    val isSaveButtonClicked = _isSaveButtonClicked.asStateFlow()

    init {
        viewModelScope.launch {
            combine(place, content)
            { place, content ->
                place.mainAddress.isNotEmpty() &&
                        content.isNotEmpty()
            }.collect {
                Timber.d("isSaveButtonEnabled: $it")
                _isSaveButtonEnabled.value = it
            }
        }
    }

    fun getPictureIntent(): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
    }

    fun addPicture(picture: PictureModel) {
        _pictures.value = pictures.value + picture
        _isPictureMax.value = pictures.value.size >= 5
    }

    fun removePicture(picture: PictureModel) {
        _pictures.value = pictures.value - picture
        _isPictureMax.value = pictures.value.size >= 5
    }

    fun setSelectedGlobe(position: Int) {
        selectedGlobe.value = globes[position]
    }

    fun setSelectedDate(date: Long) {
        _selectedDate.value = date
    }

    fun setPlace(place: PlaceModel) {
        _place.value = place
    }

    fun save() {
        if (isSaveButtonClicked.value) return
        _isSaveButtonClicked.value = true
    }

    suspend fun saveMoment() {
        viewModelScope.launch {
            saveMomentUseCase(
                moment = MomentModel(
                    place = place.value,
                    pictures = pictures.value,
                    content = content.value,
                    globes = listOf(selectedGlobe.value),
                    date = _selectedDate.value
                ).toDomain()
            )
        }.join()
    }
}