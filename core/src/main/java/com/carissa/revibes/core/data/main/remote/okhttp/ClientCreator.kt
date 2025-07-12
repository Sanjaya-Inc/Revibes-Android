package com.carissa.revibes.core.data.main.remote.okhttp

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import org.koin.core.annotation.Single
import java.util.concurrent.TimeUnit

@Single
internal class ClientCreator(
    context: Context,
    authenticator: Authenticator,
    private val okhttpCache: CacheCreator
) {

    fun create(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            cache(okhttpCache.create())
            retryOnConnectionFailure(false)
            interceptors.forEach {
                addInterceptor(it)
            }
            networkInterceptors.forEach {
                addNetworkInterceptor(it)
            }
            connectionPool(ConnectionPool(16, 6, TimeUnit.MINUTES))
            protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
        }.build()
    }

    private val interceptors = listOf(
        authenticator,
        ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250_000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    )
    private val networkInterceptors = listOf(
        createNetworkInterceptor()
    )

    private fun createNetworkInterceptor() = Interceptor { chain ->
        val response = chain.proceed(chain.request())

        response.newBuilder()
            .header("Cache-Control", "public, max-age=$CACHE_TIME_ONLINE")
            .header("Accept-Encoding", "gzip")
            .build()
    }

    companion object {
        private const val CACHE_TIME_ONLINE = 60 * 5
    }
}
