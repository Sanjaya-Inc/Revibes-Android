package com.carissa.revibes.auth.data.remote

import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.POST

interface LoginRemoteApi {
    @POST("v1/auth/login/email")
    @FormUrlEncoded
    suspend fun loginWithEmail(@Field("email") email: String, @Field("password") password: String)
}
