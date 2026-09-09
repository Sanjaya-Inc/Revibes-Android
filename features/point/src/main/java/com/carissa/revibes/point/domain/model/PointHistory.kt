package com.carissa.revibes.point.domain.model

data class PointHistory(
    val id: String,
    val timestamp: String,
    val sourceType: String?,
    val symbol: String,
    val value: Int
)
