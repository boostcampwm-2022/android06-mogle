package com.wakeup.presentation.ui.addmoment

import androidx.lifecycle.ViewModel
import com.wakeup.presentation.model.PlaceModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaceCheckViewModel : ViewModel() {

    private val _place = MutableStateFlow<PlaceModel?>(null)
    val place = _place.asStateFlow()

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl = _imageUrl.asStateFlow()

    fun setPlace(place: PlaceModel) {
        _place.value = place
    }

    fun setImageUrl(imageUrl: String?) {
        imageUrl?.let {
            _imageUrl.value = "https:$it"
        }
    }
}