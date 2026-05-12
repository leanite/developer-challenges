package com.leanite.dynaquiz.feature.splash

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashHost(
    onNavigateToNext: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    SplashEventEffects(
        events = viewModel.events,
        onNavigateToNext = onNavigateToNext,
    )

    SplashScreen(
        onAnimationFinished = { viewModel.onIntent(SplashIntent.AnimationFinished) },
    )
}