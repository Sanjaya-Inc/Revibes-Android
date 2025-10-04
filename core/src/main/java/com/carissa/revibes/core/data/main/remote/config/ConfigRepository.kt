package com.carissa.revibes.core.data.main.remote.config

import android.util.Log
import com.carissa.revibes.core.data.main.model.FeatureName
import com.carissa.revibes.core.data.utils.rethrowCancellation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class ConfigRepository(
    private val remoteConfigSource: RemoteConfigSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun initialize() = CoroutineScope(ioDispatcher).launch {
        remoteConfigSource.runCatching {
            fetchAndActivate()
        }
            .rethrowCancellation()
            .onFailure {
                Log.d(TAG, "initialize failure: $it")
            }
    }

    fun getSupportEmail(): String = remoteConfigSource.getString(SUPPORT_EMAIL_KEY)
    fun getPhoneNumber(): String = remoteConfigSource.getString(PHONE_NUMBER_KEY)
    fun getWhatsappNumber(): String = remoteConfigSource.getString(WHATSAPP_NUMBER_KEY)
    fun getFaxNumber(): String = remoteConfigSource.getString(FAX_NUMBER_KEY)

    fun getFeatureFlagKey(featureName: FeatureName): Boolean {
        return remoteConfigSource.getBoolean(featureName.getFeatureFlagKey())
    }
    fun getYourPointFeatureFlagEnabled(): Boolean =
        remoteConfigSource.getBoolean(FEATURE_FLAG_YOUR_POINT_KEY)

    fun getDropOffFeatureFlagEnabled(): Boolean = remoteConfigSource.getBoolean(FEATURE_FLAG_DROP_OFF_KEY)
    fun getPickUpFeatureFlagEnabled(): Boolean = remoteConfigSource.getBoolean(FEATURE_FLAG_PICK_UP_KEY)
    fun getExchangesFeatureFlagEnabled(): Boolean =
        remoteConfigSource.getBoolean(FEATURE_FLAG_EXCHANGES_KEY)

    fun getMyTransactionsFeatureFlagEnabled(): Boolean =
        remoteConfigSource.getBoolean(FEATURE_FLAG_MY_TRANSACTIONS_KEY)

    companion object {
        private const val TAG = "ConfigRepository"
        private const val SUPPORT_EMAIL_KEY = "support_email"
        private const val PHONE_NUMBER_KEY = "phone_number"
        private const val WHATSAPP_NUMBER_KEY = "whatsapp_number"
        private const val FAX_NUMBER_KEY = "fax_number"
        private const val FEATURE_FLAG_YOUR_POINT_KEY = "your_point_feature_flag"
        private const val FEATURE_FLAG_DROP_OFF_KEY = "drop_off_feature_flag"
        private const val FEATURE_FLAG_PICK_UP_KEY = "pick_up_feature_flag"
        private const val FEATURE_FLAG_EXCHANGES_KEY = "exchanges_feature_flag"
        private const val FEATURE_FLAG_MY_TRANSACTIONS_KEY = "my_transactions_feature_flag"
    }
}
