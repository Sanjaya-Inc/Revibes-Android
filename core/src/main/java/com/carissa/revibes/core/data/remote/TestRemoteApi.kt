package com.carissa.revibes.core.data.remote

import de.jensklingenberg.ktorfit.http.GET

interface TestRemoteApi {
    @GET("people/1/")
    suspend fun getPerson(): String
}
