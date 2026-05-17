package com.leanite.dynaquiz.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashEventEffects(
    events: Flow<SplashEvent>,
    onNavigateToNext: () -> Unit,
) {
    LaunchedEffect(events) {
        events.collectLatest { event ->
            when (event) {
                SplashEvent.NavigateToNext -> onNavigateToNext()
            }
        }
    }
}
