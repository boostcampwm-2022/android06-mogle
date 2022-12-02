package com.wakeup.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.weather.GetWeatherDataUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.LocationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherDataUseCase: GetWeatherDataUseCase
) : ViewModel() {

    fun testGetWeather() {
        viewModelScope.launch {
            getWeatherDataUseCase(
                LocationModel(
                    37.0,
                    129.0
                ).toDomain()
            )
                .mapCatching { it.toPresentation() }
                .onSuccess { weather ->
                    Timber.d("날씨: ${weather.id} ${weather.type} ${weather.temperature}")
                }
                .onFailure {
                    Timber.d("에러 $it")
                }
        }
    }
}