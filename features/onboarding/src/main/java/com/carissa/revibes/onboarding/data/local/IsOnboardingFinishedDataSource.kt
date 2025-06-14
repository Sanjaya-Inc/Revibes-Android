/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.onboarding.data.local

import com.carissa.revibes.core.data.main.local.LocalDataSource
import org.koin.core.annotation.Single

/**
 * Return type Boolean with default value false
 **/
interface IsOnboardingFinishedDataSource :
    IsOnboardingFinishedDataSourceSetter,
    IsOnboardingFinishedDataSourceGetter {
    companion object {
        const val KEY = "is_onboarding_finished"
    }
}

@Single
internal class IsOnboardingFinishedDataSourceImpl(
    private val localDataSource: LocalDataSource
) : IsOnboardingFinishedDataSource,
    IsOnboardingFinishedDataSourceSetter by IsOnboardingFinishedDataSourceSetterImpl(localDataSource),
    IsOnboardingFinishedDataSourceGetter by IsOnboardingFinishedDataSourceGetterImpl(localDataSource)
