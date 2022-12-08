package com.wakeup.presentation.ui.globe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.wakeup.domain.usecase.DeleteMomentInGlobeUseCase
import com.wakeup.domain.usecase.GetMomentsNotInGlobeUseCase
import com.wakeup.domain.usecase.InsertMomentInGlobeUseCase
import com.wakeup.presentation.mapper.toPresentation
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
    private val deleteMomentInGlobeUseCase: DeleteMomentInGlobeUseCase
) : ViewModel() {

    private val _moments = MutableStateFlow<PagingData<MomentModel>>(PagingData.empty())
    val moments = _moments.asStateFlow()

    fun fetchMomentsNotInGlobe(globeId: Long) {
        viewModelScope.launch {
            _moments.value = getMomentsNotInGlobeUseCase(globeId).map { pagingData ->
                pagingData.map { moment -> moment.toPresentation() }
            }
                .cachedIn(viewModelScope)
                .first()
        }
    }

    fun setMoments(pagingDataMoment: PagingData<MomentModel>) {
        _moments.value = pagingDataMoment
    }
}