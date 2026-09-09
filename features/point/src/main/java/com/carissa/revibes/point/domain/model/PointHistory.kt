package com.carissa.revibes.point.domain.model

enum class PointSourceKind {
    DAILY_CHECK_IN,
    DROP_OFF,
    MISSION,
    EXCHANGE,
    UNKNOWN,
}

data class PointHistory(
    val id: String,
    val timestamp: String,
    val sourceType: String?,
    val symbol: String,
    val value: Int
) {
    fun sourceKind(): PointSourceKind = when (sourceType) {
        "daily-reward" -> PointSourceKind.DAILY_CHECK_IN
        "logistic-order" -> PointSourceKind.DROP_OFF
        "mission" -> PointSourceKind.MISSION
        "exchange" -> PointSourceKind.EXCHANGE
        else -> PointSourceKind.UNKNOWN
    }

    fun signedAmount(): String {
        val sign = if (symbol == "minus") "-" else "+"
        return "$sign$value"
    }
}
