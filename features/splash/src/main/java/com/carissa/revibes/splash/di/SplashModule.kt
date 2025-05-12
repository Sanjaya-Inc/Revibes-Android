package com.carissa.revibes.splash.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.splash.data",
    "com.carissa.revibes.splash.domain",
    "com.carissa.revibes.splash.presentation"
)
class SplashModule
