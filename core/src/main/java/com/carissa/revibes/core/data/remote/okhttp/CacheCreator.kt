package com.carissa.revibes.core.data.remote.okhttp

import android.content.Context
import okhttp3.Cache
import org.koin.core.annotation.Single

@Single
class CacheCreator(
    private val context: Context
) {

    fun create(): Cache {
        return Cache(context.cacheDir, CACHE_SIZE)
    }

    companion object {
        private const val CACHE_SIZE = 50L * 1024L * 1024L
    }
}
