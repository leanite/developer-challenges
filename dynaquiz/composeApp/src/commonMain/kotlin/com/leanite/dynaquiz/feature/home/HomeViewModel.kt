package com.leanite.dynaquiz.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.core.domain.usecase.GetLastNicknameUseCase
import com.leanite.dynaquiz.core.domain.usecase.RegisterOrFetchPlayerUseCase
import com.leanite.dynaquiz.core.domain.usecase.SetLastNicknameUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class HomeViewModel(
    private val getLastNicknameUseCase: GetLastNicknameUseCase,
    private val setLastNicknameUseCase: SetLastNicknameUseCase,
    private val registerOrFetchPlayerUseCase: RegisterOrFetchPlayerUseCase,
    private val saveNicknameDelay: Duration = DEFAULT_NICKNAME_DELAY,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var saveNicknameJob: Job? = null

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Load -> load()
            is HomeIntent.NicknameChanged -> onNicknameChanged(intent.value)
            is HomeIntent.ChallengeModeSelected -> onChallengeModeSelected(intent.mode)
            HomeIntent.StartQuizClicked -> startQuiz()
            HomeIntent.RankingClicked -> _events.trySend(HomeEvent.NavigateToRanking)
        }
    }

    private fun load() {
        viewModelScope.launch {
            val lastNickname = getLastNicknameUseCase()
                .orEmpty()
                .take(HomeValidation.MAX_NICKNAME_LENGTH)
            _uiState.update { it.copy(nickname = lastNickname) }
        }
    }

    private fun onNicknameChanged(rawValue: String) {
        val safeValue = rawValue.take(HomeValidation.MAX_NICKNAME_LENGTH)
        _uiState.update { it.copy(nickname = safeValue) }

        // Cancela o job anterior, agenda um novo pra 1s
        saveNicknameJob?.cancel()
        saveNicknameJob = viewModelScope.launch {
            delay(saveNicknameDelay)
            setLastNicknameUseCase(safeValue)
        }
    }

    private fun onChallengeModeSelected(mode: ChallengeMode) {
        _uiState.update { it.copy(challengeMode = mode) }
    }

    private fun startQuiz() {
        val state = _uiState.value
        if (!state.canStart) return

        viewModelScope.launch {
            _uiState.update { it.copy(isStarting = true) }
            when (val result = registerOrFetchPlayerUseCase(state.nickname.trim())) {
                is AppResult.Success -> {
                    _events.send(HomeEvent.NavigateToQuiz(
                        playerId = result.data.id,
                        challengeMode = state.challengeMode,
                    ))
                }
                is AppResult.Error -> {
                    _events.send(HomeEvent.ShowMessage(HomeMessage.PlayerSaveError(result.error)))
                }
            }
            _uiState.update { it.copy(isStarting = false) }
        }
    }

    private companion object {
        val DEFAULT_NICKNAME_DELAY = 1.seconds
    }
}