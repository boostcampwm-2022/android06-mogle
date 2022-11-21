package com.wakeup.presentation.ui.addmoment

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.SaveMomentUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.model.PictureModel
import com.wakeup.presentation.model.PlaceModel
import com.wakeup.presentation.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddMomentViewModel @Inject constructor(
    private val saveMomentUseCase: SaveMomentUseCase
) : ViewModel() {

    // mock data
    val globes = listOf(
        GlobeModel("globe 1"),
        GlobeModel("globe 2"),
        GlobeModel("globe 3"),
    )

    val tmpGlobes = arrayOf(
        ("globe 1"),
        ("globe 2"),
        ("globe 3"),
    )

    // mock data
    val place = MutableStateFlow(
        PlaceModel(
            mainAddress = "우리집",
            detailAddress = "안동시 옥동",
            latitude = 36.567,
            longitude = 127.456,
        )
    )

    private var selectedGlobe = MutableStateFlow(globes.first())

    private val _pictures = MutableStateFlow<List<PictureModel>>(emptyList())
    val pictures = _pictures.asStateFlow()

    private val _isPictureMax = MutableStateFlow(false)
    val isPictureMax = _isPictureMax.asStateFlow()

    private val _selectedDate = MutableStateFlow(DateUtil.getToday())
    val selectedDate = _selectedDate.asStateFlow()

    val content = MutableStateFlow("")

    private val _isSaveButtonEnabled = MutableStateFlow(false)
    val isSaveButtonEnabled = _isSaveButtonEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            combine(place, selectedGlobe, content, selectedDate)
            { place, selectedGlobe, content, selectedDate ->
                place.mainAddress.isNotEmpty() &&
                        selectedGlobe.name.isNotEmpty() &&
                        content.isNotEmpty() &&
                        selectedDate.isNotEmpty()
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

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun setSelectedGlobe(position: Int) {
        selectedGlobe.value = globes[position]
    }

    fun saveMoment() {
        viewModelScope.launch {
            saveMomentUseCase(
                moment = MomentModel(
                    place = place.value,
                    pictures = pictures.value,
                    content = content.value,
                    globes = listOf(selectedGlobe.value),
                    date = selectedDate.value
                ).toDomain(),
                place = place.value.toDomain(),
                pictures = pictures.value.map { it.toDomain() },
            )
            Timber.d("${pictures.value[0]}")
        }
    }
}