/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.onboarding.data.local

import com.carissa.revibes.core.data.main.local.LocalDataSource

fun interface IsOnboardingFinishedDataSourceGetter {
    fun getIsOnboardingFinishedValue(): Boolean
}

internal class IsOnboardingFinishedDataSourceGetterImpl(
    private val localDataSource: LocalDataSource
) : IsOnboardingFinishedDataSourceGetter {
    override fun getIsOnboardingFinishedValue(): Boolean {
        return localDataSource.getBoolean(IsOnboardingFinishedDataSource.KEY, false)
    }
}
