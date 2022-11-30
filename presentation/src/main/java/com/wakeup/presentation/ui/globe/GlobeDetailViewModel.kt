package com.wakeup.presentation.ui.globe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.GetMomentsByGlobeUseCase
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.MomentModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class GlobeDetailViewModel @Inject constructor(
    private val getMomentsByGlobeUseCase: GetMomentsByGlobeUseCase
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
}