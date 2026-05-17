package com.leanite.dynaquiz.feature.splash

import androidx.compose.runtime.Immutable

@Immutable
sealed interface SplashIntent {
    data object AnimationFinished : SplashIntent
}

@Immutable
sealed interface SplashEvent {
    data object NavigateToNext : SplashEvent
}
