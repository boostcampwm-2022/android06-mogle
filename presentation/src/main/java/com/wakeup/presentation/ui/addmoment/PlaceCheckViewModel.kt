package com.wakeup.presentation.ui.addmoment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.domain.usecase.search.SearchImageUseCase
import com.wakeup.presentation.model.PlaceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceCheckViewModel @Inject constructor(
    private val searchImageUseCase: SearchImageUseCase,
) : ViewModel() {

    private val _place = MutableStateFlow<PlaceModel?>(null)
    val place = _place.asStateFlow()

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl = _imageUrl.asStateFlow()

    fun setPlace(place: PlaceModel) {
        _place.value = place
        setImageUrl(place.detailAddress + place.mainAddress)
    }

    private fun setImageUrl(address: String) {
        viewModelScope.launch {
            _imageUrl.value =
                searchImageUseCase(address).getOrNull()?.path
        }
    }
}