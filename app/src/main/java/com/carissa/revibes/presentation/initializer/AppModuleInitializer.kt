package com.carissa.revibes.presentation.initializer

import android.content.Context
import androidx.startup.Initializer
import com.carissa.revibes.core.presentation.inititalizer.KoinInitializer
import com.carissa.revibes.di.AppModule
import org.koin.core.context.loadKoinModules
import org.koin.ksp.generated.module

class AppModuleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        loadKoinModules(AppModule.module)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KoinInitializer::class.java)
    }
}
