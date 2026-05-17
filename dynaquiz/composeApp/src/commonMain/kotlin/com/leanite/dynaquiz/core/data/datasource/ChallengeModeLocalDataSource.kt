package com.leanite.dynaquiz.core.data.datasource

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toSuspendSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal interface ChallengeModeLocalDataSource {
    val storedMode: Flow<String?>

    suspend fun setStoredMode(value: String)
}

internal class ChallengeModeLocalDataSourceImpl(
    settings: Settings,
) : ChallengeModeLocalDataSource {
    @OptIn(ExperimentalSettingsApi::class)
    private val suspendSettings = settings.toSuspendSettings()

    // valor inicial síncrono via Settings normal
    private val _storedMode = MutableStateFlow(settings.getStringOrNull(SETTINGS_KEY_CHALLENGE_MODE))
    override val storedMode: Flow<String?> = _storedMode.asStateFlow()

    @OptIn(ExperimentalSettingsApi::class)
    override suspend fun setStoredMode(value: String) {
        suspendSettings.putString(SETTINGS_KEY_CHALLENGE_MODE, value)
        _storedMode.value = value
    }
}

private const val SETTINGS_KEY_CHALLENGE_MODE = "last_challenge_mode"
