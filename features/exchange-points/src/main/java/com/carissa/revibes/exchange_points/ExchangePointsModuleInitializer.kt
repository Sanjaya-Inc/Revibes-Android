package com.carissa.revibes.exchange_points

import android.content.Context
import androidx.startup.Initializer
import com.carissa.revibes.core.di.KoinInitializer
import org.koin.core.context.loadKoinModules
import org.koin.ksp.generated.module

class ExchangePointsModuleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        loadKoinModules(ExchangePointsModule.module)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KoinInitializer::class.java)
    }
}
