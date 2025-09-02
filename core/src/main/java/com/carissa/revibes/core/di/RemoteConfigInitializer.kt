package com.carissa.revibes.core.di

import android.content.Context
import androidx.startup.Initializer
import com.carissa.revibes.core.data.main.remote.config.ConfigRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemoteConfigInitializer : Initializer<Unit>, KoinComponent {

    private val configRepository: ConfigRepository by inject()

    override fun create(context: Context) {
        configRepository.initialize()
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KoinInitializer::class.java)
    }
}
