package com.leanite.dynaquiz.feature.ranking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun RankingIntentEffects(onIntent: (RankingIntent) -> Unit) {
    LaunchedEffect(Unit) {
        onIntent(RankingIntent.Load)
    }
}
