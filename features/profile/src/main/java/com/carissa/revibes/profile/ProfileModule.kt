/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.profile

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.profile.data",
    "com.carissa.revibes.profile.domain",
    "com.carissa.revibes.profile.presentation",
)
object ProfileModule
