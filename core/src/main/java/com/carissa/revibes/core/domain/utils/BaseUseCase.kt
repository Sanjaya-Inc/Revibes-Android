package com.carissa.revibes.core.domain.utils

import com.carissa.revibes.core.data.utils.ApiException
import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent

abstract class BaseUseCase(
    private val generalErrorMapper: GeneralErrorMapper = KoinJavaComponent.getKoin().get()
) {
    protected suspend fun <T> execute(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: ApiException) {
            throw mapError(e)
        } catch (e: Exception) {
            throw Throwable("Unknown error, please contact our support", e)
        }
    }

    protected open fun mapError(e: ApiException): Throwable {
        return generalErrorMapper.mapError(e)
    }
}

@Single
class GeneralErrorMapper {
    fun mapError(e: ApiException): Throwable {
        return when (e.statusCode) {
            -1 -> Throwable("Network error, please check your connection", e)
            else -> Throwable("Unknown error, please contact our support", e)
        }
    }
}
