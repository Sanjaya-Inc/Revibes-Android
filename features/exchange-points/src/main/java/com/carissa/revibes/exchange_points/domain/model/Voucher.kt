package com.carissa.revibes.exchange_points.domain.model

data class Voucher(
    val id: String,
    val name: String,
    val description: String,
    val imageUri: String,
    val point: Int,
    val quota: Int
)
