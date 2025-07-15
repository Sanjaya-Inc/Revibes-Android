package com.carissa.revibes.core.data.main.remote

import com.carissa.revibes.core.BuildConfig
import org.koin.core.annotation.Single

@Single
internal class BaseUrlProvider {
    fun getBaseUrl(): String {
        return BuildConfig.BASE_URL
    }
}
