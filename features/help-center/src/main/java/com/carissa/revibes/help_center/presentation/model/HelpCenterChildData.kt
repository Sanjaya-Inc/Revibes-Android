package com.carissa.revibes.help_center.presentation.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable

@Keep
@Stable
data class HelpCenterChildData(
    val isExpanded: Boolean = false,
    val question: String = "",
    val answer: String = ""
)
