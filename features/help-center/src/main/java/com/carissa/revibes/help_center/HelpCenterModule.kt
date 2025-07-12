/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.help_center

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.help_center.data",
    "com.carissa.revibes.help_center.domain",
    "com.carissa.revibes.help_center.presentation",
)
object HelpCenterModule
