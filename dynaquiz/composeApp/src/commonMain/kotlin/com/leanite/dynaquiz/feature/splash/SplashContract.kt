package com.leanite.dynaquiz.feature.splash

import androidx.compose.runtime.Immutable

@Immutable
data class SplashUiState(
    val progress: Float = 0f,
)

@Immutable
sealed interface SplashIntent {
    data object Start : SplashIntent
}

@Immutable
sealed interface SplashEvent {
    data object NavigateToNext : SplashEvent
}