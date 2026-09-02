package com.carissa.revibes.core.data.utils

import com.carissa.revibes.core.data.model.ErrorResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApiExceptionTest {

    @Test
    fun `formats 404 status code into clean user friendly message when error response is null`() {
        val exception = ApiException(
            statusCode = 404,
            errorResponse = null,
            cause = RuntimeException(
                "Client request(POST https://v1-via54wl6oa-et.a.run.app/news) invalid response 404 Not Found"
            )
        )

        assertEquals("Requested endpoint not found (404)", exception.message)
    }

    @Test
    fun `uses error response error field when available`() {
        val errorResponse = ErrorResponse(status = "failed", code = 400, error = "NEWS.INVALID_TITLE")
        val exception = ApiException(
            statusCode = 400,
            errorResponse = errorResponse
        )

        assertEquals("NEWS.INVALID_TITLE", exception.message)
    }

    @Test
    fun `uses error response message field when provided`() {
        val errorResponse = ErrorResponse(
            status = "failed",
            code = 400,
            error = "NEWS.INVALID_TITLE",
            message = "Invalid news title"
        )
        val exception = ApiException(
            statusCode = 400,
            errorResponse = errorResponse
        )

        assertEquals("Invalid news title", exception.message)
    }

    @Test
    fun `ErrorModel displayMessage uses message field when present`() {
        val errorModel = com.carissa.revibes.core.data.main.model.ErrorModel(
            status = "error",
            error = "AUTH.INVALID_CREDENTIALS",
            message = "Invalid email or password"
        )
        val exception = com.carissa.revibes.core.data.main.remote.ApiException(errorModel)

        assertEquals("Invalid email or password", exception.message)
    }

    @Test
    fun `ErrorModel displayMessage falls back to humanized error code when message is empty`() {
        val errorModel = com.carissa.revibes.core.data.main.model.ErrorModel(
            status = "error",
            error = "AUTH.INVALID_CREDENTIALS"
        )
        val exception = com.carissa.revibes.core.data.main.remote.ApiException(errorModel)

        assertEquals("Auth Invalid Credentials", exception.message)
    }

    @Test
    fun `formats 401 status code cleanly when error response is empty`() {
        val exception = ApiException(
            statusCode = 401,
            errorResponse = null
        )

        assertEquals("Unauthorized access. Please login again.", exception.message)
    }

    @Test
    fun `formats 500 status code cleanly when server error occurs`() {
        val exception = ApiException(
            statusCode = 500,
            errorResponse = null
        )

        assertEquals("Server error (500). Please try again later.", exception.message)
    }
}
