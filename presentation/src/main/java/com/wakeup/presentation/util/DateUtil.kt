package com.wakeup.presentation.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun getToday(): String {
        return SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(Date())
    }

    fun getDateByTime(timeMillis: Long): String {
        return SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(Date(timeMillis))
    }
}