package com.carissa.revibes.core.data.main.remote.okhttp

import com.carissa.revibes.core.data.auth.local.AuthTokenDataSource
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.annotation.Single
import kotlin.text.isNullOrBlank

@Single
internal class Authenticator(
    private val authTokenDataSource: AuthTokenDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authToken = authTokenDataSource.getAuthToken()
        if (!request.headers["Authorization"].isNullOrBlank() || authToken.isNullOrBlank()) {
            return chain.proceed(request)
        }
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $authToken")
            .build()
        return chain.proceed(newRequest)
    }
}
