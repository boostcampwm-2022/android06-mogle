package com.wakeup.presentation.extension

import android.content.res.Resources
import android.util.TypedValue
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController

fun <T> NavController.setNavigationResultToBackStack(key: String, value: T) {
    this.previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> NavController.getNavigationResultFromTop(key: String): MutableLiveData<T>? =
    this.currentBackStackEntry?.savedStateHandle?.getLiveData(key)

val Int.dp
    get() = Resources.getSystem().displayMetrics?.let { dm ->
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), dm)
    } ?: 0f
