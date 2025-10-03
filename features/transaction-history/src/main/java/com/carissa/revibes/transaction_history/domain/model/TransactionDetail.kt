package com.carissa.revibes.transaction_history.domain.model

data class TransactionDetail(
    val id: String,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val country: String,
    val items: List<TransactionDetailItem>,
    val status: String,
    val totalPoint: Int,
    val store: Store?
)

data class TransactionDetailItem(
    val id: String,
    val name: String,
    val type: String,
    val weight: Int,
    val point: Int,
    val media: List<TransactionDetailMedia>
)

data class TransactionDetailMedia(
    val downloadUri: String
)

data class Store(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val country: String,
    val address: String,
    val postalCode: String,
    val position: Position,
    val status: String
)

data class Position(
    val latitude: Double,
    val longitude: Double
)
