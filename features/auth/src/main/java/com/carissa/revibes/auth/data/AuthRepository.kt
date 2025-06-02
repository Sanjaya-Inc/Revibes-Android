package com.carissa.revibes.auth.data

import com.carissa.revibes.auth.data.remote.LoginRemoteApi
import com.carissa.revibes.auth.data.remote.createLoginRemoteApi
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.annotation.Single

interface AuthRepository : LoginRemoteApi

@Single
internal class AuthRepositoryImpl(ktorfit: Ktorfit) :
    AuthRepository,
    LoginRemoteApi by ktorfit.createLoginRemoteApi()
