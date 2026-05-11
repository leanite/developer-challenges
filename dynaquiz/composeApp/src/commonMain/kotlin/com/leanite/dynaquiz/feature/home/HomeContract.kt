package com.leanite.dynaquiz.feature.home

import androidx.compose.runtime.Immutable
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.PlayerId
import com.leanite.dynaquiz.core.domain.result.AppError

@Immutable
data class HomeUiState(
    val nickname: String = "",
    val nicknameError: NicknameError? = null,
    val challengeMode: ChallengeMode = ChallengeMode.Relaxed,
    val isStarting: Boolean = false,
) {
    val canStart: Boolean
        get() = nicknameError == null && nickname.trim().isNotEmpty() && !isStarting
}

@Immutable
sealed interface NicknameError {
    data object Empty : NicknameError
    data object TooShort : NicknameError
    data object TooLong : NicknameError //TODO: talvez eu limite a quantidade de caracteres no Input
}

@Immutable
sealed interface HomeIntent {
    data object Load : HomeIntent
    data class NicknameChanged(val value: String) : HomeIntent
    data class ChallengeModeSelected(val mode: ChallengeMode) : HomeIntent
    data object StartQuizClicked : HomeIntent
    data object RankingClicked : HomeIntent
}

@Immutable
sealed interface HomeEvent {
    data class NavigateToQuiz(
        val playerId: PlayerId,
        val challengeMode: ChallengeMode,
    ) : HomeEvent
    data object NavigateToRanking : HomeEvent
    data class ShowMessage(val type: HomeMessage) : HomeEvent
}

@Immutable
sealed interface HomeMessage {
    data class PlayerSaveError(val error: AppError) : HomeMessage
}

internal object HomeValidation {
    const val MIN_NICKNAME_LENGTH = 2
    const val MAX_NICKNAME_LENGTH = 20

    fun validateNickname(value: String): NicknameError? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> NicknameError.Empty
            trimmed.length < MIN_NICKNAME_LENGTH -> NicknameError.TooShort
            trimmed.length > MAX_NICKNAME_LENGTH -> NicknameError.TooLong
            else -> null
        }
    }
}