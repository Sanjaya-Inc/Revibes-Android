package com.carissa.revibes.point

import android.content.Context
import androidx.startup.Initializer
import com.carissa.revibes.core.di.KoinInitializer
import org.koin.core.annotation.Factory
import org.koin.core.context.loadKoinModules
import org.koin.ksp.generated.module

@Factory
class PointModuleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        loadKoinModules(PointModule.module)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KoinInitializer::class.java)
    }
}
