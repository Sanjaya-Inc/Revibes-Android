package com.carissa.revibes.core.data.remote

import android.util.Log
import io.ktor.client.plugins.logging.Logger
import org.koin.core.annotation.Single

@Single
class ApiHttpLogger : Logger {
    override fun log(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        private const val TAG = "ApiHttpLogger"
    }
}
