package com.carissa.revibes.point

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.carissa.revibes.point.data",
    "com.carissa.revibes.point.domain",
    "com.carissa.revibes.point.presentation",
)
object PointModule
