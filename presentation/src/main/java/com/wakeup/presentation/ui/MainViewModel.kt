package com.wakeup.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.moment.GetAllMomentsUseCase
import com.wakeup.domain.usecase.weather.GetWeatherDataUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.model.WeatherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    getAllMomentListUseCase: GetAllMomentsUseCase,
) : ViewModel() {

    // TODO WorkManager를 통해 지속적인 업데이트
    private val _weatherState = MutableStateFlow<UiState<WeatherModel>>(UiState.Empty)
    val weatherState = _weatherState.asStateFlow()

    var weather: WeatherModel? = null

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    var allMoments: StateFlow<List<MomentModel>>? = getAllMomentListUseCase("").map { moments ->
        moments.map { moment ->
            moment.toPresentation()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    ).apply { _isReady.value = true }

    fun fetchWeather(locationModel: LocationModel) {
        _weatherState.value = UiState.Loading

        viewModelScope.launch {
            getWeatherDataUseCase(locationModel.toDomain())
                .mapCatching { it.toPresentation() }
                .onSuccess { weatherModel ->
                    _weatherState.value = UiState.Success(weatherModel)
                    weather = weatherModel
                }.onFailure { _weatherState.value = UiState.Failure }
        }
    }
}