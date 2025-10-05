package com.carissa.revibes.exchange_points.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserVoucher(
    val id: String,
    val voucherId: String,
    val status: String,
    val claimedAt: String?,
    val expiredAt: String?,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val description: String,
    val imageUri: String,
    val code: String,
    val guides: List<String>,
    val termConditions: List<String>
)
