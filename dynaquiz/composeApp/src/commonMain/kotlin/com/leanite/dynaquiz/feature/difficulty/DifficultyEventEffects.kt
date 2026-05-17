package com.leanite.dynaquiz.feature.difficulty

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

@Composable
fun DifficultyEventEffects(
    events: Flow<DifficultyEvent>,
    onNavigateBack: () -> Unit,
) {
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                DifficultyEvent.NavigateBack -> onNavigateBack()
            }
        }
    }
}
