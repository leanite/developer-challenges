package com.leanite.dynaquiz.core.data.repository

import com.leanite.dynaquiz.core.domain.repository.UserRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toSuspendSettings

@OptIn(ExperimentalSettingsApi::class)
internal class UserRepositoryImpl(
    settings: Settings,
) : UserRepository {

    private val suspendSettings = settings.toSuspendSettings()

    override suspend fun getLastNickname(): String? =
        suspendSettings.getStringOrNull(KEY_LAST_NICKNAME)

    override suspend fun setLastNickname(nickname: String) {
        suspendSettings.putString(KEY_LAST_NICKNAME, nickname)
    }
}

private const val KEY_LAST_NICKNAME = "last_nickname"