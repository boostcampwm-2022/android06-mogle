package com.wakeup.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.naver.maps.map.util.FusedLocationSource
import com.wakeup.domain.model.SortType
import com.wakeup.domain.usecase.GetMomentListUseCase
import com.wakeup.presentation.mapper.toDomain
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.LocationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val STATE_COLLAPSED = 4

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMomentListUseCase: GetMomentListUseCase
) : ViewModel() {

    private val _moments = MutableStateFlow<PagingData<MomentModel>>(PagingData.empty())
    val moments: Flow<PagingData<MomentModel>> = _moments

    private val searchQuery = MutableStateFlow("")

    private val _scrollToTop = MutableStateFlow(false)
    val scrollToTop = _scrollToTop.asStateFlow()

    val sortType = MutableStateFlow(SortType.MOST_RECENT)
    val bottomSheetState = MutableStateFlow(STATE_COLLAPSED)

    val fetchLocationState = MutableStateFlow(false)

    private val location = MutableStateFlow<LocationModel?>(null)

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

        fetchLocationState.value = false
        location.value = null
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setScrollToTop(state: Boolean) {
        _scrollToTop.value = state
    }

    fun setLocation(location: LocationModel) {
        this.location.value = location
    }
}