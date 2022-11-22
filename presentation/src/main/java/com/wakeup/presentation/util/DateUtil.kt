package com.wakeup.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {

    private const val MOMENT_PATTERN = "yyyy년 MM월 dd일"
    private const val PREVIEW_MOMENT_PATTERN = "yyyy-MM-dd"
    private val momentSdf = SimpleDateFormat(MOMENT_PATTERN, Locale.getDefault())
    private val previewSdf = SimpleDateFormat(PREVIEW_MOMENT_PATTERN, Locale.getDefault())

    fun getToday(): String = momentSdf.format(Date())

    fun getDateByTime(timeMillis: Long): String = momentSdf.format(timeMillis)

    fun getFormattedDate(date: Long) = previewSdf.format(date)
}