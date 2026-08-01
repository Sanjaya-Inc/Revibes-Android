package com.carissa.revibes.core.data.utils

import com.carissa.revibes.core.data.model.ErrorResponse
import com.carissa.revibes.core.data.user.local.UserDataSourceGetter
import com.carissa.revibes.core.domain.usecase.TokenExpiredUseCase
import com.carissa.revibes.core.presentation.util.AppDispatchers
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent

abstract class BaseRepository(
    private val shouldKickWhenAuthFailed: Boolean = true,
    private val json: Json = KoinJavaComponent.getKoin().get(),
    private val appDispatchers: AppDispatchers = KoinJavaComponent.getKoin().get(),
    private val tokenExpiredUseCase: TokenExpiredUseCase = KoinJavaComponent.getKoin().get(),
    private val userDataGetter: UserDataSourceGetter = KoinJavaComponent.getKoin().get(),
) {
    @Suppress("ThrowsCount")
    protected suspend fun <T> execute(
        block: suspend () -> T
    ): T {
        return withContext(appDispatchers.io) {
            try {
                block()
            } catch (e: ResponseException) {
                throw parseAndWrap(e.response, e)
            } catch (e: ApiException) {
                throw e
            } catch (e: Exception) {
                throw ApiException(-1, null, e)
            }
        }
    }

    private suspend fun parseAndWrap(
        response: HttpResponse,
        cause: Throwable
    ): ApiException {
        val body = runCatching { response.bodyAsText() }.getOrNull()
        val dto = runCatching { json.decodeFromString<ErrorResponse>(body ?: "") }.getOrNull()
        return ApiException(response.status.value, dto, cause).also {
            if (shouldKickWhenAuthFailed && it.statusCode == 401 || it.isForbidden()) {
                tokenExpiredUseCase()
            }
        }
    }

    private fun ApiException.isForbidden(): Boolean {
        val isAdmin = userDataGetter.getUserValue().getOrNull()?.isAdmin() == true
        if (!isAdmin) return false
        return statusCode == 403 && (
            errorResponse?.error?.contains("COMMON.FORBIDDEN") == true ||
                errorResponse?.status?.contains("COMMON.FORBIDDEN") == true
            )
    }
}

class ApiException(
    val statusCode: Int,
    val errorResponse: ErrorResponse?,
    cause: Throwable? = null
) : Throwable(formatErrorMessage(statusCode, errorResponse, cause), cause) {
    companion object {
        private fun formatErrorMessage(
            statusCode: Int,
            errorResponse: ErrorResponse?,
            cause: Throwable?
        ): String {
            errorResponse?.message?.takeIf { it.isNotBlank() }?.let { return it }
            errorResponse?.error?.takeIf { it.isNotBlank() }?.let { return it }
            errorResponse?.status?.takeIf { it.isNotBlank() }?.let { return it }
            return when (statusCode) {
                404 -> "Requested endpoint not found (404)"
                401 -> "Unauthorized access. Please login again."
                403 -> "Access denied. You do not have permission."
                400 -> "Bad request. Please check input data."
                in 500..599 -> "Server error ($statusCode). Please try again later."
                else -> "HTTP $statusCode error"
            }
        }
    }
}
