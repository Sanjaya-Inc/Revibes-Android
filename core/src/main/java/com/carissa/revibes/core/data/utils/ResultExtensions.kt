package com.carissa.revibes.core.data.utils

import kotlin.coroutines.cancellation.CancellationException

/**
 * Checks the receiver [Result]. If it is a failure that was caused by a
 * [CancellationException], this function re-throws it. Otherwise, it does nothing.
 *
 * This allows for chaining on a [runCatching] block while preserving coroutine cancellation.
 *
 * @return The original [Result] receiver, to allow for further chaining.
 */
inline fun <T> Result<T>.rethrowCancellation(): Result<T> {
    onFailure { exception ->
        if (exception is CancellationException) {
            throw exception
        }
    }
    return this
}
