/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.core.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

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
