package com.carissa.revibes.home.data.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable

@Stable
@Keep
data class HomeBannerData(
    val imageUrl: String,
    val deeplink: String
)
