package com.leanite.dynaquiz.feature.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.core.domain.usecase.GetMyRankingUseCase
import com.leanite.dynaquiz.core.domain.usecase.GetRankingUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankingViewModel(
    private val playerName: String,
    private val getRankingUseCase: GetRankingUseCase,
    private val getMyRankingUseCase: GetMyRankingUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()

    private val _events = Channel<RankingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onIntent(intent: RankingIntent) {
        when (intent) {
            RankingIntent.Load -> loadCurrentTab()
            is RankingIntent.TabSelected -> selectTab(intent.tab)
            RankingIntent.BackPressed -> _events.trySend(RankingEvent.NavigateBack)
        }
    }

    private fun selectTab(tab: RankingTab) {
        if (_uiState.value.selectedTab == tab) return
        _uiState.update { it.copy(selectedTab = tab) }
        loadCurrentTab()
    }

    private fun loadCurrentTab() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result =
                when (_uiState.value.selectedTab) {
                    RankingTab.All -> getRankingUseCase()
                    RankingTab.Mine -> getMyRankingUseCase(playerName)
                }
            when (result) {
                is AppResult.Success ->
                    _uiState.update {
                        it.copy(entries = result.data.toImmutableList(), isLoading = false)
                    }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, entries = persistentListOf()) }
                    _events.send(RankingEvent.ShowMessage(RankingMessage.LoadFailed))
                }
            }
        }
    }
}
