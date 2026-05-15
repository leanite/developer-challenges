package com.leanite.dynaquiz.feature.home

import androidx.compose.runtime.Immutable
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.PlayerId
import com.leanite.dynaquiz.core.domain.result.AppError

@Immutable
data class HomeUiState(
    val nickname: String = "",
    val challengeMode: ChallengeMode = ChallengeMode.Timed.Easy,
    val isStarting: Boolean = false,
) {
    val canStart: Boolean
        get() = nickname.trim().length >= HomeValidation.MIN_NICKNAME_LENGTH && !isStarting
}

@Immutable
sealed interface HomeIntent {
    data object Load : HomeIntent
    data class NicknameChanged(val value: String) : HomeIntent
    data object StartQuizClicked : HomeIntent
    data object DifficultyClicked : HomeIntent
    data object RankingClicked : HomeIntent
}

@Immutable
sealed interface HomeEvent {
    data class NavigateToQuiz(
        val playerName: String,
        val challengeMode: ChallengeMode,
    ) : HomeEvent
    data object NavigateToDifficulty : HomeEvent
    data class NavigateToRanking(val playerName: String) : HomeEvent
    data class ShowMessage(val type: HomeMessage) : HomeEvent
}

@Immutable
sealed interface HomeMessage {
    data class PlayerSaveError(val error: AppError) : HomeMessage
}

internal object HomeValidation {
    const val MIN_NICKNAME_LENGTH = 3
    const val MAX_NICKNAME_LENGTH = 15
}