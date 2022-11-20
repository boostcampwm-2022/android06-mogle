package com.wakeup.presentation.ui.addmoment

import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.wakeup.presentation.model.PlaceModel
import com.wakeup.presentation.util.DateUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddMomentViewModel : ViewModel() {

    // mock data
    val globes = arrayOf("globe 1", "globe 2", "globe 3")

    // mock data
    val place = PlaceModel("우리집", "안동시 옥동")

    private var selectedGlobe = globes.first()

    private val _pictures = MutableStateFlow<List<Bitmap>>(emptyList())
    val pictures = _pictures.asStateFlow()

    private val _isPictureMax = MutableStateFlow(false)
    val isPictureMax = _isPictureMax.asStateFlow()

    private val _selectedDate = MutableStateFlow(DateUtil.getToday())
    val selectedDate = _selectedDate.asStateFlow()

    val content = MutableStateFlow("")

    fun getPictureIntent(): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
    }

    fun addPicture(picture: Bitmap) {
        _pictures.value = pictures.value + picture
        _isPictureMax.value = pictures.value.size >= 5
    }

    fun removePicture(picture: Bitmap) {
        _pictures.value = pictures.value - picture
        _isPictureMax.value = pictures.value.size >= 5
    }

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun setSelectedGlobe(position: Int) {
        selectedGlobe = globes[position]
    }
}