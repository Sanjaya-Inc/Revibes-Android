package com.carissa.revibes.auth.presentation.initializer

import android.content.Context
import androidx.startup.Initializer
import com.carissa.revibes.auth.di.AuthModule
import com.carissa.revibes.core.presentation.inititalizer.KoinInitializer
import org.koin.core.context.loadKoinModules
import org.koin.ksp.generated.module

class AuthModuleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        loadKoinModules(AuthModule.module)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KoinInitializer::class.java)
    }
}
