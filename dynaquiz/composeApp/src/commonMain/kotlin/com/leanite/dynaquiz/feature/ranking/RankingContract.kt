package com.leanite.dynaquiz.feature.ranking

import androidx.compose.runtime.Immutable
import com.leanite.dynaquiz.core.domain.model.RankingEntry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class RankingUiState(
    val selectedTab: RankingTab = RankingTab.All,
    val entries: ImmutableList<RankingEntry> = persistentListOf(),
    val isLoading: Boolean = false,
)

enum class RankingTab { All, Mine }

@Immutable
sealed interface RankingIntent {
    data object Load : RankingIntent

    data class TabSelected(
        val tab: RankingTab,
    ) : RankingIntent

    data object BackPressed : RankingIntent
}

@Immutable
sealed interface RankingEvent {
    data object NavigateBack : RankingEvent

    data class ShowMessage(
        val type: RankingMessage,
    ) : RankingEvent
}

@Immutable
sealed interface RankingMessage {
    data object LoadFailed : RankingMessage
}
