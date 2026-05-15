package com.leanite.dynaquiz.feature.result

import androidx.compose.runtime.Immutable
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult

@Immutable
data class ResultUiState(
    val result: QuizSessionResult,
)

@Immutable
sealed interface ResultIntent {
    data object HomeClicked : ResultIntent
    data object RankingClicked : ResultIntent
}

@Immutable
sealed interface ResultEvent {
    data object NavigateToHome : ResultEvent
    data class NavigateToRanking(val playerName: String) : ResultEvent
}