package com.carissa.revibes.core.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {
    fun getTodayDate(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        return formatter.format(currentDate)
    }
}
