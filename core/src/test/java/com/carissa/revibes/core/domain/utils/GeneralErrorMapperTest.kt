package com.carissa.revibes.core.domain.utils

import com.carissa.revibes.core.data.utils.ApiException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.SocketTimeoutException

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
}
