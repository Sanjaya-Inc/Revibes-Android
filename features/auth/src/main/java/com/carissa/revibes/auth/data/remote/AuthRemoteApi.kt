package com.carissa.revibes.auth.data.remote

import com.carissa.revibes.auth.data.model.DeviceRegistrationRequest
import com.carissa.revibes.auth.data.model.LoginResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import org.koin.core.annotation.Single

interface AuthRemoteApi {
    @POST("auth/login/email")
    @FormUrlEncoded
    suspend fun loginWithEmail(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("auth/login/phone")
    @FormUrlEncoded
    suspend fun loginWithPhone(
        @Field("phone") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("auth/signup/email")
    @FormUrlEncoded
    suspend fun signUpWithEmail(
        @Field("email") email: String,
        @Field("displayName") displayName: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("password") password: String
    )

    @POST("auth/signup/email")
    @FormUrlEncoded
    suspend fun signUpWithPhone(
        @Field("phone") phone: String,
        @Field("displayName") displayName: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("password") password: String
    )

    @PUT("me/devices")
    suspend fun registerDevice(
        @Body request: DeviceRegistrationRequest,
        @Header("Content-Type") contentType: String = "application/json",
    )
}

// Cleanup / Change Tester Account
// Role -> di atas admin
// Untuk Ubah Role
// Forgot Pass
// Edit Point -> Direct
// User -> Filter voucher disabled
// Upload Foto ketika drop off
@Single
internal class AuthRemoteApiImpl(ktorfit: Ktorfit) :
    AuthRemoteApi by ktorfit.createAuthRemoteApi()
