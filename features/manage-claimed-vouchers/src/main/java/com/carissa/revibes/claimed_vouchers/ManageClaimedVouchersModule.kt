/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.claimed_vouchers

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.claimed_vouchers.data",
    "com.carissa.revibes.claimed_vouchers.domain",
    "com.carissa.revibes.claimed_vouchers.presentation",
)
object ManageClaimedVouchersModule
