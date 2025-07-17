/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.manage_users

import android.content.Context
import androidx.startup.Initializer
import com.carissa.revibes.core.di.KoinInitializer
import org.koin.core.context.loadKoinModules
import org.koin.ksp.generated.module

class ManageUsersModuleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        loadKoinModules(ManageUsersModule.module)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KoinInitializer::class.java)
    }
}
