package com.wakeup.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.wakeup.domain.usecase.GetAllMomentsUseCase
import com.wakeup.domain.usecase.weather.GetWeatherDataUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.model.MomentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    getAllMomentListUseCase: GetAllMomentsUseCase,
) : ViewModel() {

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
    )
        .apply { _isReady.value = true }

    fun testGetWeather() {
        viewModelScope.launch {
            getWeatherDataUseCase(
                LocationModel(
                    36.0981,
                    129.3343
                ).toDomain()
            )
                .mapCatching { it.toPresentation() }
                .onSuccess { weather ->
                    Timber.d("날씨: ${weather.id} ${weather.type.name} ${weather.temperature}")
                }
                .onFailure {
                    Timber.d("에러 $it")
                }
        }
    }
}