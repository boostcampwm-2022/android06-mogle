package com.wakeup.presentation.extension

import android.content.res.Resources
import android.util.TypedValue
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}