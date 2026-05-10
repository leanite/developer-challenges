package com.leanite.dynaquiz.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SplashIntentEffects(onIntent: (SplashIntent) -> Unit) {
    LaunchedEffect(Unit) {
        onIntent(SplashIntent.Start)
    }
}