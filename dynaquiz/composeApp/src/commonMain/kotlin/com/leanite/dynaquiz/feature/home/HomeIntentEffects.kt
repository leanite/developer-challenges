package com.leanite.dynaquiz.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun HomeIntentEffects(onIntent: (HomeIntent) -> Unit) {
    LaunchedEffect(Unit) {
        onIntent(HomeIntent.Load)
    }
}
