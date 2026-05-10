package com.leanite.dynaquiz.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SplashHost(
    onNavigateToNext: () -> Unit,
    viewModel: SplashViewModel = viewModel { SplashViewModel() },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SplashIntentEffects(onIntent = viewModel::onIntent)
    SplashEventEffects(
        events = viewModel.events,
        onNavigateToNext = onNavigateToNext,
    )

    SplashScreen(uiState = uiState)
}