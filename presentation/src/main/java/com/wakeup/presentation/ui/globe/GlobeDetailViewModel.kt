package com.wakeup.presentation.ui.globe

import androidx.lifecycle.ViewModel
import com.wakeup.domain.usecase.globe.GetMomentsByGlobeUseCase
import com.wakeup.presentation.model.MomentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GlobeDetailViewModel @Inject constructor(
    private val getMomentsByGlobeUseCase: GetMomentsByGlobeUseCase
) : ViewModel() {
    private val _moments = MutableStateFlow<List<MomentModel>>(emptyList())
    val moments = _moments.asStateFlow()

    fun fetchMomentsByGlobe(globeId: Long) {
//        viewModelScope.launch {
//            _moments.value = getMomentsByGlobeUseCase(globeId).map { moments ->
//                moments.map { moment -> moment.toPresentation() }
//            }.first()
//        }
    }
}