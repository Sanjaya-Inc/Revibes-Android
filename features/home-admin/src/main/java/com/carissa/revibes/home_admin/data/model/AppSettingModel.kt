package com.carissa.revibes.home_admin.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AppSettingPointData(
    val organic: Int = 5,
    @SerialName("non-organic") val nonOrganic: Int = 5,
    val b3: Int = 5
)

@Keep
@Serializable
data class AppSettingDailyRewardData(
    val days: Int = 7,
    val initialPoint: Int = 5,
    val multiplier: Int = 5
)

@Keep
@Serializable
data class AppSettingData(
    val point: AppSettingPointData = AppSettingPointData(),
    val dailyReward: AppSettingDailyRewardData = AppSettingDailyRewardData()
)

@Keep
@Serializable
data class AppSettingResponse(
    val code: Int = 200,
    val message: String = "",
    val data: AppSettingData? = null,
    val status: String = ""
)
