package com.carissa.revibes.manage_transaction.domain.model

data class ManageTransactionDomain(
    val id: String,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val country: String,
    val address: String,
    val postalCode: String,
    val storeName: String,
    val status: TransactionStatus,
    val maker: String,
    val items: List<TransactionItemDomain>,
    val totalPoint: Int
)

data class TransactionItemDomain(
    val id: String,
    val name: String,
    val type: String,
    val weight: Int,
    val point: Int,
    val media: List<TransactionMediaDomain>
)

data class TransactionMediaDomain(
    val uploadUrl: String,
    val downloadUri: String,
    val expiredAt: Long
)

data class TransactionDetailDomain(
    val id: String,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val country: String,
    val address: String,
    val postalCode: String,
    val storeName: String,
    val items: List<TransactionDetailItemDomain>,
    val status: TransactionStatus,
    val totalPoint: Int
)

data class TransactionDetailItemDomain(
    val id: String,
    val name: String,
    val type: String,
    val weight: Int,
    val point: Int,
    val media: List<TransactionDetailMediaDomain>
)

data class TransactionDetailMediaDomain(
    val uploadUrl: String,
    val downloadUri: String,
)
