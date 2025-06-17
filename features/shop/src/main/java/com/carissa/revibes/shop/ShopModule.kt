/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.shop

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.shop.data",
    "com.carissa.revibes.shop.domain",
    "com.carissa.revibes.shop.presentation",
)
object ShopModule
