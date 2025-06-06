/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.onboarding.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.onboarding.data",
    "com.carissa.revibes.onboarding.domain",
    "com.carissa.revibes.onboarding.presentation",
)
object OnboardingModule
