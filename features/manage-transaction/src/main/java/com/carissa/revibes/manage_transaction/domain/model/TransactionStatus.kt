package com.carissa.revibes.manage_transaction.domain.model

enum class TransactionStatus(val value: String) {
    PENDING("pending"),
    COMPLETED("completed"),
    REJECTED("rejected");

    companion object {
        fun fromString(value: String): TransactionStatus {
            return entries.find { it.value == value } ?: PENDING
        }
    }
}
