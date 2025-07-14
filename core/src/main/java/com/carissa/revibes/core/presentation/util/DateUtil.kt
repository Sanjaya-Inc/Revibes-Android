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

    fun formatDate(isoDateString: String): String {
        return try {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val outputFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
            val date = inputFormatter.parse(isoDateString)
            date?.let { outputFormatter.format(it) } ?: isoDateString
        } catch (e: Exception) {
            isoDateString
        }
    }
}
