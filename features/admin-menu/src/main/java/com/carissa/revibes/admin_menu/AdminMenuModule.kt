/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.admin_menu

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.admin_menu.data",
    "com.carissa.revibes.admin_menu.domain",
    "com.carissa.revibes.admin_menu.presentation",
)
object AdminMenuModule
