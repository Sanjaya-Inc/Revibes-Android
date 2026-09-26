package com.carissa.revibes.home_admin.data.model

import androidx.annotation.Keep
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AppSettingPointData(
    @EncodeDefault val organic: Int = 5,
    @SerialName("non-organic") @EncodeDefault val nonOrganic: Int = 5,
    @EncodeDefault val b3: Int = 5
)

@Keep
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AppSettingDailyRewardData(
    @EncodeDefault val days: Int = 7,
    @EncodeDefault val initialPoint: Int = 1,
    @EncodeDefault val multiplier: Int = 0,
    @EncodeDefault val bannerText: String = "Check in 30 Days & Get Voucher Rp25k"
)

@Keep
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AppSettingData(
    @EncodeDefault val point: AppSettingPointData = AppSettingPointData(),
    @EncodeDefault val dailyReward: AppSettingDailyRewardData = AppSettingDailyRewardData()
)

@Keep
@Serializable
data class AppSettingResponse(
    val code: Int = 200,
    val message: String = "",
    val data: AppSettingData? = null,
    val status: String = ""
)
