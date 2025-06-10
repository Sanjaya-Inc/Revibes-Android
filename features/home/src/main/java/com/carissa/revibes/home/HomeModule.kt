/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.home.data",
    "com.carissa.revibes.home.domain",
    "com.carissa.revibes.home.presentation",
)
object HomeModule
