package com.wakeup.presentation.ui.addmoment

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.globe.GetGlobesUseCase
import com.wakeup.domain.usecase.moment.SaveMomentUseCase
import com.wakeup.domain.usecase.moment.UpdateMomentUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.model.PictureModel
import com.wakeup.presentation.model.PlaceModel
import com.wakeup.presentation.ui.UiState
import com.wakeup.presentation.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AddMomentViewModel @Inject constructor(
    private val getGlobesUseCase: GetGlobesUseCase,
    private val saveMomentUseCase: SaveMomentUseCase,
    private val updateMomentUseCase: UpdateMomentUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val argMoment = savedStateHandle.get<MomentModel?>("moment")

    private val _state: MutableStateFlow<UiState<Boolean>> = MutableStateFlow(UiState.Empty)
    val state = _state.asStateFlow()

    private val _pictures = MutableStateFlow<List<PictureModel>>(emptyList())
    val pictures = _pictures.asStateFlow()

    private val _isPictureMax = MutableStateFlow(false)
    val isPictureMax = _isPictureMax.asStateFlow()

    private val _globes = MutableStateFlow(emptyList<GlobeModel>())
    val globes = _globes.asStateFlow()

    private var _selectedGlobe = MutableStateFlow(GlobeModel(name = "", thumbnail = null))
    var selectedGlobe = _selectedGlobe.asStateFlow()

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
            getGlobesUseCase().collectLatest {
                _globes.value = it.map { globe -> globe.toPresentation() }
                _selectedGlobe.value = _globes.value.first()
            }
        }

        viewModelScope.launch {
            combine(place, content)
            { place, content ->
                place.mainAddress.isNotEmpty() &&
                        content.isNotEmpty()
            }.collect {
                _isSaveButtonEnabled.value = it
            }
        }

        argMoment?.let { setMoment(it) }
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
        _selectedGlobe.value = globes.value[position]
    }

    fun setSelectedDate(date: Long) {
        _selectedDate.value = date
    }

    fun setPlace(place: PlaceModel) {
        _place.value = place
    }

    private fun setMoment(moment: MomentModel) {
        with(moment) {
            // TODO: 글로브, 사진 처리 필요
            _selectedGlobe.value = globes.first()
            pictures.forEach { addPicture(it) }
            _selectedDate.value = date
            _place.value = place
        }
        content.value = moment.content
    }

    fun save() {
        if (isSaveButtonClicked.value) return
        _isSaveButtonClicked.value = true
    }

    suspend fun saveMoment() {
        _state.value = UiState.Loading
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
        _state.value = UiState.Success(true)
    }

    suspend fun updateMoment() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            updateMomentUseCase(
                MomentModel(
                    id = argMoment?.id ?: ERROR_ID,
                    place = place.value,
                    pictures = pictures.value,
                    content = content.value,
                    globes = listOf(selectedGlobe.value),
                    date = _selectedDate.value
                ).toDomain()
            )
        }.join()
        _state.value = UiState.Success(true)
    }

    private companion object {
        const val ERROR_ID = -1L
    }
}