package com.leanite.dynaquiz.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashHost(
    onNavigateToNext: () -> Unit,
    titleModifier: Modifier = Modifier,
    purpleSurfaceModifier: Modifier = Modifier,
    viewModel: SplashViewModel = koinViewModel(),
) {
    SplashEventEffects(
        events = viewModel.events,
        onNavigateToNext = onNavigateToNext,
    )

    SplashScreen(
        titleModifier = titleModifier,
        purpleSurfaceModifier = purpleSurfaceModifier,
        onAnimationFinished = { viewModel.onIntent(SplashIntent.AnimationFinished) },
    )
}
