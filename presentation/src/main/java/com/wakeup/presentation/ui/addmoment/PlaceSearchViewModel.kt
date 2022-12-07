package com.wakeup.presentation.ui.addmoment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.search.SearchPlaceUseCase
import com.wakeup.presentation.mapper.toPresentation
import com.wakeup.presentation.model.PlaceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    searchPlaceUseCase: SearchPlaceUseCase
) : ViewModel() {

    val searchText = MutableStateFlow("")

    private val _searchResult = MutableStateFlow<List<PlaceModel>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    private val _isNetworkError = MutableStateFlow(false)
    val isNetworkError = _isNetworkError.asStateFlow()

    init {
        viewModelScope.launch {
            searchText.debounce(500).collect {
                _isNetworkError.value = false
                if (it.isEmpty()) {
                    _searchResult.value = emptyList()
                } else {
                    searchPlaceUseCase(it)
                        .onSuccess { places ->
                            _searchResult.value = places.map { place -> place.toPresentation() }
                        }.onFailure {
                            _isNetworkError.value = true
                        }
                }
            }
        }
    }
}
