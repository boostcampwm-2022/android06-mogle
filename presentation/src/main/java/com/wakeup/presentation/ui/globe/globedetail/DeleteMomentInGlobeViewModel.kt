package com.wakeup.presentation.ui.globe.globedetail

import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.wakeup.domain.usecase.globe.DeleteMomentInGlobeUseCase
import com.wakeup.domain.usecase.globe.GetMomentsByGlobeUseCase
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
class DeleteMomentInGlobeViewModel @Inject constructor(
    private val getMomentsInGlobeUseCase: GetMomentsByGlobeUseCase,
    private val deleteMomentInGlobeUseCase: DeleteMomentInGlobeUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _moments = MutableStateFlow<PagingData<MomentModel>>(PagingData.empty())
    val moments = _moments.asStateFlow()

    private val _deleteReadyMoments = MutableStateFlow<List<MomentModel>>(emptyList())
    private val deleteReadyMoments = _deleteReadyMoments.asStateFlow()

    val isNotExistDeleteReadyMoments = deleteReadyMoments.map { moments ->
        moments.isEmpty()
    }.asLiveData()

    private val argsGlobe = savedStateHandle.get<GlobeModel>(ARGS_GlOBE)
        ?: GlobeModel(name = AddMomentInGlobeViewModel.ARGS_GlOBE, thumbnail = null)

    init {
        fetchMomentsInGlobe()
    }

    private fun fetchMomentsInGlobe() {
        viewModelScope.launch {
            _moments.value = getMomentsInGlobeUseCase(argsGlobe.id).map { pagingDataMoments ->
                pagingDataMoments.map { moment -> moment.toPresentation() }
            }
                .cachedIn(viewModelScope)
                .first()
        }
    }

    fun setDeleteReadyMoments(moment: MomentModel) {
        if (moment.isSelected.not()) {
            _deleteReadyMoments.value = deleteReadyMoments.value
                .filter { deletedMoment -> deletedMoment.id != moment.id }
        } else {
            _deleteReadyMoments.value = deleteReadyMoments.value
                .toMutableList()
                .apply { add(moment) }
                .toList()
        }
    }

    fun deleteMoments(view: View) {
        viewModelScope.launch {
            launch {
                deleteMomentInGlobeUseCase(
                    deleteReadyMoments.value.map { moment -> moment.toDomain() },
                    argsGlobe.toDomain()
                )
            }.join()
            view.findNavController().navigateUp()
        }
    }

    companion object {
        const val ARGS_GlOBE = "globe"
    }
}