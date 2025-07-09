/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.drop_off

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.drop_off.data",
    "com.carissa.revibes.drop_off.domain",
    "com.carissa.revibes.drop_off.presentation",
)
object DropOffModule
