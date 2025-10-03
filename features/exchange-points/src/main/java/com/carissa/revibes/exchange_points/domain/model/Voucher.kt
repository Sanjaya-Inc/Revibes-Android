package com.carissa.revibes.exchange_points.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Voucher(
    val id: String,
    val name: String,
    val description: String,
    val imageUri: String,
    val point: Int,
    val quota: Int,
    val validUntil: String = "Valid until: 31 Dec 2025",
    val terms: List<String> = DUMMY_TERMS,
)

private val DUMMY_TERMS = listOf(
    "Valid for one-time redemption",
    "Cannot be exchanged for cash or other items",
    "Valid only at Revibes merchants/partners",
    "Valid for 30 days from the date of claim",
    "While supplies last"
)
