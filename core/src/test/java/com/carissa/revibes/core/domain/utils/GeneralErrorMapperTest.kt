package com.carissa.revibes.core.domain.utils

import com.carissa.revibes.core.data.model.ErrorResponse
import com.carissa.revibes.core.data.utils.ApiException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GeneralErrorMapperTest {
    private val mapper = GeneralErrorMapper()

    @Test
    fun `status -1 with read failure shows cause`() {
        val mapped = mapper.mapError(
            ApiException(-1, null, IllegalStateException("Unable to read image"))
        )
        assertEquals("Unable to read image", mapped.message)
    }

    @Test
    fun `status -1 with timeout stays generic network error`() {
        val mapped = mapper.mapError(
            ApiException(-1, null, SocketTimeoutException("timeout"))
        )
        assertEquals("Network error, please check your connection", mapped.message)
    }

    @Test
    fun `status -1 with unknown host stays generic network error`() {
        val mapped = mapper.mapError(
            ApiException(-1, null, UnknownHostException("revibes.example"))
        )
        assertEquals("Network error, please check your connection", mapped.message)
    }

    @Test
    fun `status -1 with connection failure stays generic network error`() {
        val mapped = mapper.mapError(
            ApiException(-1, null, ConnectException("failed to connect"))
        )
        assertEquals("Network error, please check your connection", mapped.message)
    }

    @Test
    fun `status -1 with blank cause stays generic network error`() {
        val mapped = mapper.mapError(
            ApiException(-1, null, IllegalStateException("  "))
        )
        assertEquals("Network error, please check your connection", mapped.message)
    }

    @Test
    fun `http error uses server message`() {
        val mapped = mapper.mapError(
            ApiException(400, ErrorResponse(message = "Unable to estimate points"))
        )
        assertEquals("Unable to estimate points", mapped.message)
    }
}
