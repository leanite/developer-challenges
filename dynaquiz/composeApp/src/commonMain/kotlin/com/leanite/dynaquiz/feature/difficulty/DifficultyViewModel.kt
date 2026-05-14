package com.leanite.dynaquiz.feature.difficulty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leanite.dynaquiz.core.domain.usecase.GetLastChallengeModeUseCase
import com.leanite.dynaquiz.core.domain.usecase.SetLastChallengeModeUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DifficultyViewModel(
    private val getLastChallengeMode: GetLastChallengeModeUseCase,
    private val setLastChallengeMode: SetLastChallengeModeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DifficultyUiState())
    val uiState: StateFlow<DifficultyUiState> = _uiState.asStateFlow()

    private val _events = Channel<DifficultyEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onIntent(intent: DifficultyIntent) {
        when (intent) {
            DifficultyIntent.Load -> load()
            is DifficultyIntent.ModeSelected -> _uiState.update { it.copy(selectedMode = intent.mode) }
            DifficultyIntent.ConfirmClicked -> confirm()
            DifficultyIntent.BackClicked -> _events.trySend(DifficultyEvent.NavigateBack)
        }
    }

    private fun load() {
        viewModelScope.launch {
            val current = getLastChallengeMode().first()
            _uiState.update { it.copy(selectedMode = current) }
        }
    }

    private fun confirm() {
        val mode = _uiState.value.selectedMode
        viewModelScope.launch {
            _uiState.update { it.copy(isConfirming = true) }
            setLastChallengeMode(mode)
            _events.send(DifficultyEvent.NavigateBack)
            _uiState.update { it.copy(isConfirming = false) }
        }
    }
}