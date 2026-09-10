package com.carissa.revibes.manage_transaction.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AppSettingPointData(
    val organic: Int = 5,
    @SerialName("non-organic") val nonOrganic: Int = 5,
    val b3: Int = 5
) {
    fun forType(type: String): Int {
        return when (type) {
            "organic" -> organic
            "non-organic" -> nonOrganic
            "b3" -> b3
            else -> 5
        }
    }
}

@Keep
@Serializable
data class AppSettingData(
    val point: AppSettingPointData = AppSettingPointData()
)

@Keep
@Serializable
data class AppSettingResponse(
    val code: Int = 200,
    val message: String = "",
    val data: AppSettingData? = null,
    val status: String = ""
)
