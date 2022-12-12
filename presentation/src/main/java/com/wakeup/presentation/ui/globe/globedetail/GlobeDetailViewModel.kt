package com.wakeup.presentation.ui.globe.globedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.wakeup.domain.usecase.globe.DeleteGlobeUseCase
import com.wakeup.domain.usecase.globe.GetMomentsByGlobeUseCase
import com.wakeup.domain.usecase.globe.UpdateGlobeUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.model.MomentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobeDetailViewModel @Inject constructor(
    private val getMomentsByGlobeUseCase: GetMomentsByGlobeUseCase,
    private val updateGlobeUseCase: UpdateGlobeUseCase,
    private val deleteGlobeUseCase: DeleteGlobeUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    lateinit var moments: Flow<PagingData<MomentModel>>

    private val argsGlobe = savedStateHandle
        .get<GlobeModel>(ARGS_GlOBE) ?: GlobeModel(name = "", thumbnail = null)

    private val _isExistMoment = MutableStateFlow(false)
    val isExistMoment = _isExistMoment.asStateFlow()

    init {
        fetchMomentsByGlobe(argsGlobe.id)
    }

    private fun fetchMomentsByGlobe(globeId: Long) {
        moments = getMomentsByGlobeUseCase(globeId).map { pagingData ->
            pagingData.map { moment ->
                moment.toPresentation()
            }
        }.cachedIn(viewModelScope)
    }

    fun setMomentExist(isExist: Boolean) {
        _isExistMoment.value = isExist
    }

    fun updateGlobeTitle(name: String) {
        viewModelScope.launch {
            updateGlobeUseCase(argsGlobe.copy(name = name).toDomain())
        }
    }

    fun deleteGlobe() {
        viewModelScope.launch {
            deleteGlobeUseCase(argsGlobe.toDomain())
        }
    }

    companion object {
        const val ARGS_GlOBE = "globe"
    }
}