package com.wakeup.presentation.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    private const val PATTERN = "yyyy년 MM월 dd일"

    fun getToday(): String {
        return SimpleDateFormat(PATTERN, Locale.getDefault()).format(Date())
    }

    fun getDateByTime(timeMillis: Long): String {
        return SimpleDateFormat(PATTERN, Locale.getDefault()).format(Date(timeMillis))
    }
}