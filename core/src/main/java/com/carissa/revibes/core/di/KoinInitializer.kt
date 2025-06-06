package com.carissa.revibes.core.di

import android.content.Context
import androidx.startup.Initializer
import com.tencent.mmkv.MMKV
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class KoinInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        MMKV.initialize(context)
        startKoin {
            androidLogger()
            androidContext(context)
            modules(listOf(CoreModule.module))
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}
