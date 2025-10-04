package com.carissa.revibes.core.data.main.model

import androidx.compose.runtime.Stable

@Stable
enum class FeatureName(val title: String, val prefixKey: String) {
    YOUR_POINT("Point", "your_point"),
    DROP_OFF("Drop off", "drop_off"),
    PICK_UP("Pick up", "pick_up"),
    EXCHANGES("Exchange Points", "exchanges"),
    MY_TRANSACTIONS("Transaction History", "my_transactions");

    fun getFeatureFlagKey(): String {
        return "${prefixKey}_feature_flag"
    }
}
