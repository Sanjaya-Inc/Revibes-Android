package com.carissa.revibes.auth

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.auth.data",
    "com.carissa.revibes.auth.domain",
    "com.carissa.revibes.auth.presentation"
)
object AuthModule
