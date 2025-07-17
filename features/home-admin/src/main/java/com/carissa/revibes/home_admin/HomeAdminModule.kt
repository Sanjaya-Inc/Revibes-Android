/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home_admin

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.home_admin.data",
    "com.carissa.revibes.home_admin.domain",
    "com.carissa.revibes.home_admin.presentation",
)
object HomeAdminModule
