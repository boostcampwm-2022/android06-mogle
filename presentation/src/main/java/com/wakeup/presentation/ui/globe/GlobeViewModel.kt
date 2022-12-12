package com.wakeup.presentation.ui.globe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.globe.CreateGlobeUseCase
import com.wakeup.domain.usecase.globe.GetGlobesUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.GlobeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobeViewModel @Inject constructor(
    private val getGlobesUseCase: GetGlobesUseCase,
    private val createGlobesUseCase: CreateGlobeUseCase,
) : ViewModel() {

    private val _globes = MutableStateFlow<List<GlobeModel>>(emptyList())
    val globes = _globes.asStateFlow()

    fun fetchGlobes() {
        viewModelScope.launch {
            _globes.value = getGlobesUseCase().map { globes ->
                globes.map { globe -> globe.toPresentation() }
            }.first()
        }
    }

    fun createGlobe(globeName: String) {
        viewModelScope.launch {
            createGlobesUseCase(GlobeModel(name = globeName, thumbnail = null).toDomain())
            fetchGlobes()
        }
    }

    // caution: fetchGlobes 를 한 상태에서 바로 써야지 확인이 가능합니다.
    fun isExistGlobe(globeName: String): Boolean {
        return globeName in globes.value.map { globe -> globe.name }
    }
}