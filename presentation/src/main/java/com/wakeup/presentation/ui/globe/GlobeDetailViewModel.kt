package com.wakeup.presentation.ui.globe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.GetMomentsByGlobeUseCase
import com.wakeup.domain.usecase.UpdateGlobeUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.model.MomentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobeDetailViewModel @Inject constructor(
    private val getMomentsByGlobeUseCase: GetMomentsByGlobeUseCase,
    private val updateGlobeUseCase: UpdateGlobeUseCase,
) : ViewModel() {

    private val _moments = MutableStateFlow<List<MomentModel>>(emptyList())
    val moments = _moments.asStateFlow()

    fun fetchMomentsByGlobe(globeId: Long) {
        viewModelScope.launch {
            _moments.value = getMomentsByGlobeUseCase(globeId).map { moments ->
                moments.map { moment -> moment.toPresentation() }
            }.first()
        }
    }

    fun updateGlobeTitle(originGlobe: GlobeModel, name: String) {
        viewModelScope.launch {
            updateGlobeUseCase(originGlobe.copy(name = name).toDomain())
        }
    }
}