/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.manage_transaction

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.manage_transaction.data",
    "com.carissa.revibes.manage_transaction.domain",
    "com.carissa.revibes.manage_transaction.presentation",
)
object ManageTransactionModule
