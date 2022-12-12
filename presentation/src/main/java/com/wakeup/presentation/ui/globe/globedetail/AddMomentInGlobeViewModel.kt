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
import com.wakeup.domain.usecase.GetMomentsNotInGlobeUseCase
import com.wakeup.domain.usecase.globe.InsertMomentInGlobeUseCase
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
class AddMomentInGlobeViewModel @Inject constructor(
    private val getMomentsNotInGlobeUseCase: GetMomentsNotInGlobeUseCase,
    private val insertMomentInGlobeUseCase: InsertMomentInGlobeUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _moments = MutableStateFlow<PagingData<MomentModel>>(PagingData.empty())
    val moments = _moments.asStateFlow()

    private val _saveReadyMoments = MutableStateFlow<List<MomentModel>>(emptyList())
    val saveReadyMoments = _saveReadyMoments.asStateFlow()

    val isNotExistSaveReadyMoments = saveReadyMoments.map { moments ->
        moments.isEmpty()
    }.asLiveData()

    val saveReadyMomentsCount = saveReadyMoments.map { moments ->
        moments.count()
    }.asLiveData()

    private val _isExistMoment = MutableStateFlow(false)
    val isExistMoment = _isExistMoment.asStateFlow()

    private val argsGlobe = savedStateHandle.get<GlobeModel>(ARGS_GlOBE)
        ?: GlobeModel(name = ARGS_GlOBE, thumbnail = null)

    init {
        fetchMomentsNotInGlobe()
    }

    private fun fetchMomentsNotInGlobe() {
        viewModelScope.launch {
            _moments.value = getMomentsNotInGlobeUseCase(argsGlobe.id).map { pagingDataMoments ->
                pagingDataMoments.map { moment -> moment.toPresentation() }
            }
                .cachedIn(viewModelScope)
                .first()
        }
    }

    fun setSaveReadyMoments(moment: MomentModel) {
        if (moment.isSelected.not()) {
            _saveReadyMoments.value = saveReadyMoments.value
                .filter { savedMoment -> savedMoment.id != moment.id }
        } else {
            _saveReadyMoments.value = _saveReadyMoments.value
                .toMutableList()
                .apply { add(moment) }
                .toList()
        }
    }

    fun saveMomentsInGlobe(view: View) {
        viewModelScope.launch {
            launch {
                insertMomentInGlobeUseCase(
                    saveReadyMoments.value.map { moment -> moment.toDomain() },
                    argsGlobe.toDomain()
                )
            }.join()
            view.findNavController().navigateUp()
        }
    }

    fun setMomentExist(isExist: Boolean) {
        _isExistMoment.value = isExist
    }

    companion object {
        const val ARGS_GlOBE = "globe"
    }
}