package com.wakeup.presentation.extension

import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
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


fun View.getFadeOutAnimator(animDuration: Long): ObjectAnimator =
    ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f).apply {
        duration = animDuration
    }

fun View.getFadeInAnimator(animDuration: Long): ObjectAnimator =
    ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f).apply {
        duration = animDuration
    }

fun ObjectAnimator.setListener(listener: AnimatorListener): ObjectAnimator {
    this.addListener(listener)
    return this
}