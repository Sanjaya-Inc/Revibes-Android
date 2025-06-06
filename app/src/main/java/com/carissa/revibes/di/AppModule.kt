package com.carissa.revibes.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.data",
    "com.carissa.revibes.domain",
    "com.carissa.revibes.presentation",
)
object AppModule
