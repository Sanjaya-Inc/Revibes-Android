package com.carissa.revibes.auth.data.remote

import com.carissa.revibes.auth.data.model.DeviceRegistrationRequest
import com.carissa.revibes.auth.data.model.LoginResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import org.koin.core.annotation.Single

interface AuthRemoteApi {
    @POST("auth/login/email")
    @FormUrlEncoded
    suspend fun loginWithEmail(@Field("email") email: String, @Field("password") password: String): LoginResponse

    @POST("auth/signup/email")
    @FormUrlEncoded
    suspend fun signUpWithEmail(
        @Field("email") email: String,
        @Field("displayName") displayName: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("password") password: String
    )

    @PUT("me/devices")
    suspend fun registerDevice(@Body request: DeviceRegistrationRequest)
}

@Single
internal class AuthRemoteApiImpl(ktorfit: Ktorfit) :
    AuthRemoteApi by ktorfit.createAuthRemoteApi()
