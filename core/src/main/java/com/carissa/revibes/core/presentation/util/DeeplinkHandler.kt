/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.core.presentation.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import androidx.core.net.toUri

interface DeeplinkHandler {
    fun openUrl(url: String)
}

@Single
class DeeplinkHandlerImpl : DeeplinkHandler, KoinComponent {

    private val context: Context by inject()

    override fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Handle error - could log or show toast
            e.printStackTrace()
        }
    }
}

fun Context.openWhatsAppChat(phoneNumber: String) {
    // Clean the number (e.g., remove spaces or dashes)
    val cleanedNumber = phoneNumber.replace("[^\\d+]".toRegex(), "")

    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = "https://wa.me/$cleanedNumber".toUri() // E.g., wa.me/6281234567890
            setPackage("com.whatsapp") // Ensure it opens in WhatsApp only
        }
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
    }
}

fun Context.openSupportWhatsApp() {
    openWhatsAppChat("08161805621")
}
