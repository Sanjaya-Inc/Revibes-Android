package com.carissa.revibes.core.data.main.remote

import android.util.Log
import com.carissa.revibes.core.data.main.model.ErrorModel
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class ApiErrorValidator(private val json: Json) {

    suspend fun validate(response: HttpResponse) {
        try {
            // Check if the response body contains an error model
            val responseText = response.bodyAsText()
            if (responseText.contains("\"error\"") || responseText.contains("\"status\"")) {
                val errorModel = json.decodeFromString<ErrorModel>(responseText)
                if (errorModel.error.isNotEmpty()) {
                    throw ApiException(errorModel)
                }
            }
        } catch (e: Exception) {
            // Only throw ApiException, ignore other exceptions during validation
            if (e is ApiException) throw e
            Log.d("KtorfitCreator", "Response validation error: ${e.message}")
        }
    }
}
