package com.wakeup.presentation.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.wakeup.domain.model.SortType
import com.wakeup.domain.usecase.GetMomentListUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.LocationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STATE_COLLAPSED = 4

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getMomentListUseCase: GetMomentListUseCase
) : ViewModel() {

    private val _moments = MutableStateFlow<PagingData<MomentModel>>(PagingData.empty())
    val moments: Flow<PagingData<MomentModel>> = _moments

    private val searchQuery = MutableStateFlow("")

    val sortType = MutableStateFlow(SortType.MOST_RECENT)
    val bottomSheetState = MutableStateFlow(STATE_COLLAPSED)
    val location = MutableStateFlow<LocationModel?>(null)

    init {
        fetchMoments()
    }

    fun fetchMoments() {
        viewModelScope.launch {
            _moments.value = getMomentListUseCase(
                sortType = sortType.value,
                query = searchQuery.value,
                myLocation = location.value?.toDomain()
            ).map { pagingMoments ->
                pagingMoments.map { moment ->
                    moment.toPresentation()
                }
            }
                .cachedIn(viewModelScope)
                .first()
        }


    }
}