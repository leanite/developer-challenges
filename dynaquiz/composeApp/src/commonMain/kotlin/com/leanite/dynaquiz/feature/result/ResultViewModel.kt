package com.leanite.dynaquiz.feature.result

import androidx.lifecycle.ViewModel
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ResultViewModel(
    private val sessionResult: QuizSessionResult,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResultUiState(result = sessionResult))
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    private val _events = Channel<ResultEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onIntent(intent: ResultIntent) {
        when (intent) {
            ResultIntent.HomeClicked -> _events.trySend(ResultEvent.NavigateToHome)
            ResultIntent.RankingClicked ->
                _events.trySend(
                    ResultEvent.NavigateToRanking(playerName = sessionResult.setup.playerName),
                )
        }
    }
}
