/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.manage_voucher

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.manage_voucher.data",
    "com.carissa.revibes.manage_voucher.domain",
    "com.carissa.revibes.manage_voucher.presentation"
)
object ManageVoucherModule
