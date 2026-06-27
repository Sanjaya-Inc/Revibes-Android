package com.carissa.revibes

import android.content.Context
import androidx.startup.Initializer
import com.carissa.revibes.core.di.KoinInitializer
import org.koin.core.context.loadKoinModules

class AppModuleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        loadKoinModules(AppModule.module())
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KoinInitializer::class.java)
    }
}
