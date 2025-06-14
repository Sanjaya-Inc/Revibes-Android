/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.onboarding.data.local

import androidx.core.content.edit
import com.carissa.revibes.core.data.main.local.LocalDataSource

fun interface IsOnboardingFinishedDataSourceSetter {
    fun setIsOnboardingFinishedValue(value: Boolean)
}

internal class IsOnboardingFinishedDataSourceSetterImpl(
    private val localDataSource: LocalDataSource
) : IsOnboardingFinishedDataSourceSetter {
    override fun setIsOnboardingFinishedValue(value: Boolean) {
        localDataSource.edit {
            putBoolean(IsOnboardingFinishedDataSource.KEY, value)
        }
    }
}
