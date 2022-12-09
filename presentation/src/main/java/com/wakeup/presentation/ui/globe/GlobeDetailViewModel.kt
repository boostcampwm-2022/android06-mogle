package com.wakeup.presentation.ui.globe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.wakeup.domain.usecase.globe.GetMomentsByGlobeUseCase
import com.wakeup.domain.usecase.globe.UpdateGlobeUseCase
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _moments = MutableStateFlow<PagingData<MomentModel>>(PagingData.empty())
    val moments = _moments.asStateFlow()
    private val argsGlobeId = savedStateHandle.get<GlobeModel>(ARGS_GlOBE)?.id

    init {
        fetchMomentsByGlobe(argsGlobeId ?: ERROR_ID)
    }

    private fun fetchMomentsByGlobe(globeId: Long) {
        viewModelScope.launch {
            _moments.value = getMomentsByGlobeUseCase(globeId).map { pagingData ->
                pagingData.map { moment -> moment.toPresentation() }
            }
                .cachedIn(viewModelScope)
                .first()
        }
    }

    fun updateGlobeTitle(originGlobe: GlobeModel, name: String) {
        viewModelScope.launch {
            updateGlobeUseCase(originGlobe.copy(name = name).toDomain())
        }
    }

    companion object {
        const val ARGS_GlOBE = "globe"
        const val ERROR_ID = -1L
    }
}