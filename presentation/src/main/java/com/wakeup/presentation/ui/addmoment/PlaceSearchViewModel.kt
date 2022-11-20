package com.wakeup.presentation.ui.addmoment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.PlaceSearchUseCase
import com.wakeup.presentation.model.PlaceModel
import com.wakeup.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    placeSearchUseCase: PlaceSearchUseCase
) : ViewModel() {

    val searchText = MutableStateFlow("")

    private val _searchResult = MutableStateFlow<List<PlaceModel>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    init {
        viewModelScope.launch {
            searchText.debounce(500).collect {
                if (it.isEmpty()) return@collect
                _searchResult.value = placeSearchUseCase(it).map { place ->
                    place.toPresentation()
                }
            }
        }
    }
}


