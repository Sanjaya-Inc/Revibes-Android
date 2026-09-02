package com.carissa.revibes.home_admin.data

import com.carissa.revibes.core.presentation.util.AppDispatchers
import com.carissa.revibes.home_admin.domain.GoogleMapsPoint
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import java.net.HttpURLConnection
import java.net.URI

@Single
class GoogleMapsLinkResolver(
    private val appDispatchers: AppDispatchers
) {
    suspend fun resolve(input: String): Pair<Double, Double>? {
        GoogleMapsPoint.parse(input)?.let { return it }
        val url = GoogleMapsPoint.extractUrl(input) ?: return null
        if (!GoogleMapsPoint.isShortLink(url)) return null
        val expanded = withContext(appDispatchers.io) {
            runCatching { expand(url) }.getOrNull()
        } ?: return null
        return GoogleMapsPoint.parse(expanded)
    }

    private fun expand(url: String): String {
        val connection = URI(url).toURL().openConnection() as HttpURLConnection
        connection.instanceFollowRedirects = true
        connection.connectTimeout = TIMEOUT_MS
        connection.readTimeout = TIMEOUT_MS
        connection.requestMethod = "GET"
        connection.setRequestProperty("User-Agent", USER_AGENT)
        try {
            connection.connect()
            val stream = if (connection.responseCode >= HTTP_ERROR) {
                connection.errorStream
            } else {
                connection.inputStream
            }
            stream?.close()
            return connection.url.toString()
        } finally {
            connection.disconnect()
        }
    }

    private companion object {
        private const val TIMEOUT_MS = 8_000
        private const val HTTP_ERROR = 400
        private const val USER_AGENT =
            "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 Chrome/120.0.0.0 Mobile Safari/537.36"
    }
}
