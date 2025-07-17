/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.manage_users

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.manage_users.data",
    "com.carissa.revibes.manage_users.domain",
    "com.carissa.revibes.manage_users.presentation",
)
object ManageUsersModule
