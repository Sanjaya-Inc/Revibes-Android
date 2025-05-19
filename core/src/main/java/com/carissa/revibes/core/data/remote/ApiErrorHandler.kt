package com.carissa.revibes.core.data.remote

import com.carissa.revibes.core.data.model.ErrorModel
import io.ktor.client.plugins.HttpCallValidatorConfig
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.koin.core.annotation.Single

/**
 * Handles API errors by converting HTTP exceptions into ApiExceptions with structured error models.
 * This class is responsible for parsing error responses and creating appropriate error models.
 */
@Single
class ApiErrorHandler(private val json: Json) {

    /**
     * Configures the HTTP call validator to handle response exceptions.
     */
    fun configure(plugin: HttpCallValidatorConfig) {
        plugin.handleResponseExceptionWithRequest { exception, request ->
            val errorModel = createErrorModel(exception, request)
            throw ApiException(errorModel)
        }
    }

    /**
     * Creates an appropriate error model based on the exception type and request context.
     */
    private fun createErrorModel(exception: Throwable, request: HttpRequest): ErrorModel {
        return try {
            when (exception) {
                is ResponseException -> handleResponseException(exception)
                else -> createGenericErrorModel(exception, request)
            }
        } catch (e: Exception) {
            if (e is ApiException) throw e
            createFallbackErrorModel(e)
        }
    }

    /**
     * Handles ResponseException by parsing the response body.
     */
    private fun handleResponseException(exception: ResponseException): ErrorModel {
        val errorBody = runBlocking { exception.response.bodyAsText() }
        return try {
            parseErrorResponse(exception.response, errorBody)
        } catch (_: Exception) {
            createResponseFallbackModel(exception.response, errorBody)
        }
    }

    /**
     * Parses the error response body into an ErrorModel.
     */
    private fun parseErrorResponse(response: HttpResponse, errorBody: String): ErrorModel {
        val errorMap = parseErrorBody(errorBody)

        // Extract fields from the parsed map
        val status = errorMap["status"]?.toString()?.replace("\"", "")
            ?: response.status.value.toString()
        val error = errorMap["error"]?.toString()?.replace("\"", "")
            ?: response.status.description
        val reasons = parseReasons(errorMap, errorBody)

        return ErrorModel(status = status, error = error, reasons = reasons)
    }

    /**
     * Parses the error body into a map.
     */
    private fun parseErrorBody(errorBody: String): Map<String, Any> {
        return json.parseToJsonElement(errorBody).let {
            if (it is JsonObject) {
                it.toMap()
            } else {
                mapOf("error" to errorBody)
            }
        }
    }

    /**
     * Parses the reasons field from the error map.
     */
    private fun parseReasons(errorMap: Map<String, Any>, errorBody: String): List<String> {
        return errorMap["reasons"]?.let {
            if (it.toString().startsWith("[") && it.toString().endsWith("]")) {
                it.toString().removeSurrounding("[", "]")
                    .split(",")
                    .map { reason -> reason.trim().removeSurrounding("\"") }
            } else {
                listOf(it.toString().replace("\"", ""))
            }
        } ?: listOf(errorBody.take(100))
    }

    /**
     * Creates a fallback error model when response parsing fails.
     */
    private fun createResponseFallbackModel(response: HttpResponse, errorBody: String): ErrorModel {
        return ErrorModel(
            status = response.status.value.toString(),
            error = response.status.description,
            reasons = listOf(errorBody.take(100))
        )
    }

    /**
     * Creates a generic error model for non-response exceptions.
     */
    private fun createGenericErrorModel(exception: Throwable, request: HttpRequest): ErrorModel {
        return ErrorModel(
            status = "Unknown",
            error = exception.message ?: "Unknown error",
            reasons = listOf("Request to ${request.url} failed")
        )
    }

    /**
     * Creates a fallback error model for unexpected exceptions in our error handling.
     */
    private fun createFallbackErrorModel(exception: Throwable): ErrorModel {
        return ErrorModel(
            status = "Unknown",
            error = exception.message ?: "Unknown error",
            reasons = listOf("Failed to process error response")
        )
    }
}
