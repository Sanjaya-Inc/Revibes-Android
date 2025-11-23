package com.carissa.revibes.manage_users.domain.usecase

import com.carissa.revibes.core.data.main.remote.config.RemoteConfigSource
import com.carissa.revibes.core.data.user.local.UserDataSourceGetter
import com.carissa.revibes.core.domain.utils.BaseUseCase
import org.koin.core.annotation.Single

@Single
class RolesEditorWhitelistedUseCase(
    private val userDataSourceGetter: UserDataSourceGetter,
    private val remoteConfigSource: RemoteConfigSource
) : BaseUseCase() {

    operator fun invoke(): Boolean {
        return try {
            val user = userDataSourceGetter.getUserValue().getOrNull() ?: return false
            val whitelistRaw = remoteConfigSource.getString(KEY)

            if (whitelistRaw.isBlank()) return false

            val whitelist = whitelistRaw
                .split(',')
                .map { it.trim().lowercase() }
                .filter { it.isNotBlank() }
                .toSet()

            val email = user.email.trim().lowercase()
            val phone = user.phoneNumber.trim()

            email in whitelist || phone in whitelist
        } catch (_: Exception) {
            false
        }
    }

    companion object {
        private const val KEY = "roles_editor_whitelist"
    }
}
