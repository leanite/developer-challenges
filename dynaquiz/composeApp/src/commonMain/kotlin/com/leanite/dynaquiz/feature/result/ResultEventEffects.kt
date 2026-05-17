package com.leanite.dynaquiz.feature.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun ResultEventEffects(
    events: Flow<ResultEvent>,
    onNavigateToHome: () -> Unit,
    onNavigateToRanking: (String) -> Unit,
) {
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                ResultEvent.NavigateToHome -> onNavigateToHome()
                is ResultEvent.NavigateToRanking -> onNavigateToRanking(event.playerName)
            }
        }
    }
}
