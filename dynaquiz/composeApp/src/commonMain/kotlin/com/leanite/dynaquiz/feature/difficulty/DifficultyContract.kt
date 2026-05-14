package com.leanite.dynaquiz.feature.difficulty

import androidx.compose.runtime.Immutable
import com.leanite.dynaquiz.core.domain.model.ChallengeMode

@Immutable
data class DifficultyUiState(
    val selectedMode: ChallengeMode = ChallengeMode.Timed.Easy,
    val isConfirming: Boolean = false,
)

@Immutable
sealed interface DifficultyIntent {
    data object Load : DifficultyIntent
    data class ModeSelected(val mode: ChallengeMode) : DifficultyIntent
    data object ConfirmClicked : DifficultyIntent
    data object BackClicked : DifficultyIntent
}

@Immutable
sealed interface DifficultyEvent {
    data object NavigateBack : DifficultyEvent
}