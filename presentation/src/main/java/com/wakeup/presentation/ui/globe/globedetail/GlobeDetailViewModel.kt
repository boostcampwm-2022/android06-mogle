package com.wakeup.presentation.ui.globe.globedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.wakeup.domain.usecase.globe.DeleteGlobeUseCase
import com.wakeup.domain.usecase.globe.GetGlobesUseCase
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobeDetailViewModel @Inject constructor(
    private val getMomentsByGlobeUseCase: GetMomentsByGlobeUseCase,
    private val getGlobesUseCase: GetGlobesUseCase,
    private val updateGlobeUseCase: UpdateGlobeUseCase,
    private val deleteGlobeUseCase: DeleteGlobeUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _globes = MutableStateFlow<List<GlobeModel>>(emptyList())
    val globes = _globes.asStateFlow()

    lateinit var moments: Flow<PagingData<MomentModel>>

    private val argsGlobe = savedStateHandle
        .get<GlobeModel>(ARGS_GlOBE) ?: GlobeModel(name = "", thumbnail = null)

    private val _isExistMoment = MutableStateFlow(false)
    val isExistMoment = _isExistMoment.asStateFlow()

    init {
        fetchGlobes()
        fetchMomentsByGlobe(argsGlobe.id)
    }

    private fun fetchMomentsByGlobe(globeId: Long) {
        moments = getMomentsByGlobeUseCase(globeId).map { pagingData ->
            pagingData.map { moment ->
                moment.toPresentation()
            }
        }.cachedIn(viewModelScope)
    }

    private fun fetchGlobes() {
        viewModelScope.launch {
            _globes.value = getGlobesUseCase().map { globes ->
                globes.map { globe -> globe.toPresentation() }
            }.first()
        }
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

    // caution: fetchGlobes 를 한 상태에서 바로 써야지 확인이 가능합니다.
    fun isExistGlobe(globeName: String): Boolean {
        return globeName in globes.value.map { globe -> globe.name }
    }

    companion object {
        const val ARGS_GlOBE = "globe"
    }
}