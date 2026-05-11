package com.leanite.dynaquiz.core.domain.repository

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toSuspendSettings

interface UserRepository {
    suspend fun getLastNickname(): String?
    suspend fun setLastNickname(nickname: String)
}
