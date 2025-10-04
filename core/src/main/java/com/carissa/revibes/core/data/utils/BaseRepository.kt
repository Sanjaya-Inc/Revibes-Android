package com.carissa.revibes.core.data.utils

import com.carissa.revibes.core.data.model.ErrorResponse
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent

abstract class BaseRepository(
    private val json: Json = KoinJavaComponent.getKoin().get()
) {
    @Suppress("ThrowsCount")
    protected suspend fun <T> execute(
        block: suspend () -> T
    ): T {
        return try {
            block()
        } catch (e: ClientRequestException) {
            throw parseAndWrap(e.response, e)
        } catch (e: ServerResponseException) {
            throw parseAndWrap(e.response, e)
        } catch (e: RedirectResponseException) {
            throw parseAndWrap(e.response, e)
        } catch (e: Exception) {
            throw ApiException(-1, null, e)
        }
    }

    private suspend fun parseAndWrap(
        response: HttpResponse,
        cause: Throwable
    ): ApiException {
        val body = runCatching { response.bodyAsText() }.getOrNull()
        val dto = runCatching { json.decodeFromString<ErrorResponse>(body ?: "") }.getOrNull()
        return ApiException(response.status.value, dto, cause)
    }
}

class ApiException(
    val statusCode: Int,
    val errorResponse: ErrorResponse?,
    cause: Throwable? = null
) : Throwable(errorResponse?.error ?: cause?.message, cause)
