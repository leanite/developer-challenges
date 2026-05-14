package com.leanite.dynaquiz.feature.difficulty

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun DifficultyIntentEffects(onIntent: (DifficultyIntent) -> Unit) {
    LaunchedEffect(Unit) {
        onIntent(DifficultyIntent.Load)
    }
}