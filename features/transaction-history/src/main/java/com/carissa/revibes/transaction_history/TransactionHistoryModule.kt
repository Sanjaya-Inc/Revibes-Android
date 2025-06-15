/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.transaction_history

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.transaction_history.data",
    "com.carissa.revibes.transaction_history.domain",
    "com.carissa.revibes.transaction_history.presentation",
)
object TransactionHistoryModule
