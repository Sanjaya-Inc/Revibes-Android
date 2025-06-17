/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.exchange_points

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.exchange_points.data",
    "com.carissa.revibes.exchange_points.domain",
    "com.carissa.revibes.exchange_points.presentation",
)
object ExchangePointsModule
