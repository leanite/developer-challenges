package com.leanite.dynaquiz.feature.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
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
    onNavigateToRanking: (String) -> Unit,
) {
    LaunchedEffect(events) {
        events.collectLatest { event ->
            when (event) {
                is HomeEvent.NavigateToQuiz -> onNavigateToQuiz(event.playerName, event.challengeMode)

                HomeEvent.NavigateToDifficulty -> onNavigateToDifficulty()

                is HomeEvent.NavigateToRanking -> onNavigateToRanking(event.playerName)

                is HomeEvent.ShowMessage -> {
                    val text = resolveMessage(event.type)
                    snackbarHostState.showSnackbar(text)
                }
            }
        }
    }
}

private suspend fun resolveMessage(message: HomeMessage): String =
    when (message) {
        // getString() é suspend
        is HomeMessage.PlayerSaveError -> getString(HomeRes.SaveErrorGeneric)
    }
