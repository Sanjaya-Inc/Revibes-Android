package com.carissa.revibes.home_admin.data.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AppSettingModelTest {

    private val json = Json {
        prettyPrint = true
        isLenient = false
        ignoreUnknownKeys = true
    }

    @Test
    fun `put body keeps default point rates and daily reward`() {
        val body = json.encodeToString(
            AppSettingData(
                point = AppSettingPointData(organic = 5, nonOrganic = 12, b3 = 5),
                dailyReward = AppSettingDailyRewardData()
            )
        )
        val root = json.parseToJsonElement(body).jsonObject
        val point = root.getValue("point").jsonObject
        val dailyReward = root.getValue("dailyReward").jsonObject

        assertEquals(5, point.getValue("organic").jsonPrimitive.int)
        assertEquals(12, point.getValue("non-organic").jsonPrimitive.int)
        assertEquals(5, point.getValue("b3").jsonPrimitive.int)
        assertEquals(7, dailyReward.getValue("days").jsonPrimitive.int)
        assertEquals(1, dailyReward.getValue("initialPoint").jsonPrimitive.int)
        assertEquals(0, dailyReward.getValue("multiplier").jsonPrimitive.int)
        assertEquals("Check in 30 Days & Get Voucher Rp25k", dailyReward.getValue("bannerText").jsonPrimitive.content)
    }
}
