package com.wakeup.presentation.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.wakeup.domain.model.SortType
import com.wakeup.domain.usecase.GetMomentListUseCase
import com.wakeup.domain.usecase.SaveMomentUseCase
import com.wakeup.presentation.factory.MomentFactory
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.PictureModel
import com.wakeup.presentation.model.PlaceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getMomentListUseCase: GetMomentListUseCase,
    private val saveMomentUseCase: SaveMomentUseCase
) : ViewModel() {

    private val _moments = MutableStateFlow<PagingData<MomentModel>>(PagingData.empty())
    val moments: Flow<PagingData<MomentModel>> = _moments

    init {
        fetchMoments(SortType.MOST_RECENT)
    }

    fun fetchMoments(sortType: SortType) {
        viewModelScope.launch {
            _moments.value = getMomentListUseCase("", sortType).map { pagingMoments ->
                pagingMoments.map { moment ->
                    moment.toPresentation()
                }
            }
                .cachedIn(viewModelScope)
                .first()
        }
    }

    fun test() {
        viewModelScope.launch {
            MomentFactory.createMoments(10).map { moment ->
                saveMomentUseCase(
                    moment.toDomain(),
                    MomentFactory.createPlace(0).toDomain(),
                    null
                )
            }
        }
    }
}