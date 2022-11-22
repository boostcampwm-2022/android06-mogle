package com.wakeup.presentation.ui.addmoment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.SearchPlaceUseCase
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

    init {
        viewModelScope.launch {
            searchText.debounce(500).collect {
                _searchResult.value = if (it.isEmpty()) {
                    emptyList()
                } else {
                    searchPlaceUseCase(it).map { place -> place.toPresentation() }
                }
            }
        }
    }
}
