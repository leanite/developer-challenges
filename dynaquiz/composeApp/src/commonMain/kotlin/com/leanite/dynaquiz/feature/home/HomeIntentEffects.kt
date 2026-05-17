package com.leanite.dynaquiz.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

// TODO: se ficar pequeno assim, incorporar em HomeScreen diretamente
@Composable
fun HomeIntentEffects(onIntent: (HomeIntent) -> Unit) {
    LaunchedEffect(Unit) {
        onIntent(HomeIntent.Load)
    }
}
