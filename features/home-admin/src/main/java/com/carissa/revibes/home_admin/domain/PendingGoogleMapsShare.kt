package com.carissa.revibes.home_admin.domain

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.annotation.Single

@Single
class PendingGoogleMapsShare {
    private val incomingShares = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val incoming: SharedFlow<String> = incomingShares.asSharedFlow()

    fun offer(text: String) {
        incomingShares.tryEmit(text)
    }
}
