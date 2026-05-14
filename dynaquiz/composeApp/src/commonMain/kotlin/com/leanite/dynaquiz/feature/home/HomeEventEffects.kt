package com.leanite.dynaquiz.feature.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.PlayerId
import com.leanite.dynaquiz.feature.home.res.HomeRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.getString

@Composable
fun HomeEventEffects(
    events: Flow<HomeEvent>,
    snackbarHostState: SnackbarHostState,
    onNavigateToQuiz: (String, ChallengeMode) -> Unit,
    onNavigateToDifficulty: () -> Unit,
    onNavigateToRanking: () -> Unit,
) {
    LaunchedEffect(events) {
        events.collectLatest { event ->
            when (event) {
                is HomeEvent.NavigateToQuiz -> {
                    onNavigateToQuiz(event.playerName, event.challengeMode)
                }
                HomeEvent.NavigateToDifficulty -> {
                    onNavigateToDifficulty()
                }
                HomeEvent.NavigateToRanking -> {
                    onNavigateToRanking()
                }
                is HomeEvent.ShowMessage -> { //TODO: temporário
                    val text = resolveMessage(event.type)
                    snackbarHostState.showSnackbar(text)
                }
            }
        }
    }
}

private suspend fun resolveMessage(message: HomeMessage): String = when (message) {
    // getString() é suspend
    is HomeMessage.PlayerSaveError -> getString(HomeRes.SaveErrorGeneric)
}