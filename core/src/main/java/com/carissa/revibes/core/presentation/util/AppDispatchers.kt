package com.carissa.revibes.core.presentation.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import org.koin.core.annotation.Single

@Single
class AppDispatchers {
    val io: CoroutineDispatcher = Dispatchers.IO
    val main: MainCoroutineDispatcher = Dispatchers.Main
    val default: CoroutineDispatcher = Dispatchers.Default
}
