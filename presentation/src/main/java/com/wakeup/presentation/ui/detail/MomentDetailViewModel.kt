package com.wakeup.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.DeleteMomentUseCase
import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.ui.globe.globedetail.AddMomentInGlobeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MomentDetailViewModel @Inject constructor(
    private val deleteMomentUseCase: DeleteMomentUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // TODO State 저장할 필요 고민

    fun deleteMoment(momentId: Long) {
        viewModelScope.launch {
            deleteMomentUseCase(momentId)
        }
    }
}