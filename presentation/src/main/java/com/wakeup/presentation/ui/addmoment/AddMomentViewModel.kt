package com.wakeup.presentation.ui.addmoment

import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddMomentViewModel : ViewModel() {

    fun getPictureIntent(): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
    }

    val globes = arrayOf("globe 1", "globe 2", "globe 3")

    private var selectedGlobe = globes.first()

    private val _pictures = MutableStateFlow<List<Bitmap>>(emptyList())
    val pictures: StateFlow<List<Bitmap>> = _pictures

    private val _isPictureMax = MutableStateFlow(false)
    val isPictureMax: StateFlow<Boolean> = _isPictureMax


    fun addPicture(picture: Bitmap) {
        _pictures.value = pictures.value + picture
        _isPictureMax.value = pictures.value.size >= 5
    }

    fun removePicture(picture: Bitmap) {
        _pictures.value = pictures.value - picture
        _isPictureMax.value = pictures.value.size >= 5
    }

    fun setSelectedGlobe(position: Int) {
        selectedGlobe = globes[position]
    }
}