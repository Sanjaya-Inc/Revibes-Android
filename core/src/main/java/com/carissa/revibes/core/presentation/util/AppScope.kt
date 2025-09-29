package com.carissa.revibes.core.presentation.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.Single

@Single
class AppScope(
    private val appDispatchers: AppDispatchers
) : CoroutineScope by CoroutineScope(SupervisorJob() + appDispatchers.io)
