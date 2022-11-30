package com.wakeup.presentation.ui.globe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.CreateGlobeUseCase
import com.wakeup.domain.usecase.GetGlobesUseCase
import com.wakeup.domain.usecase.GetMomentCountByGlobeUseCase
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
    private val getMomentCountByGlobeUseCase: GetMomentCountByGlobeUseCase,
) : ViewModel() {

    private val _globes = MutableStateFlow<List<GlobeModel>>(emptyList())
    val globes = _globes.asStateFlow()

    fun fetchGlobes() {
        viewModelScope.launch {
            _globes.value = getGlobesUseCase().map { globes ->
                globes.map { globe ->
                    globe.toPresentation(getMomentCountByGlobeUseCase(globe.id).first().toString())
                }
            }.first()
        }
    }

    fun createGlobe(globeName: String) {
        viewModelScope.launch {
            createGlobesUseCase(GlobeModel(name = globeName).toDomain())
            fetchGlobes()
        }
    }
}