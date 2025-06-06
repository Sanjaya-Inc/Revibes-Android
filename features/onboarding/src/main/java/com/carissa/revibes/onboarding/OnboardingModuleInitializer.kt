/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.onboarding

import android.content.Context
import androidx.startup.Initializer
import com.carissa.revibes.core.di.KoinInitializer
import org.koin.core.context.loadKoinModules
import org.koin.ksp.generated.module

class OnboardingModuleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        loadKoinModules(OnboardingModule.module)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KoinInitializer::class.java)
    }
}
