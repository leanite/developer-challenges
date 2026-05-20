package com.leanite.dynaquiz.feature.ranking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.QuizPerformance
import com.leanite.dynaquiz.core.domain.model.QuizSetup
import com.leanite.dynaquiz.core.domain.model.RankingEntry
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant

@Composable
fun RankingList(
    entries: ImmutableList<RankingEntry>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(entries, key = { _, entry -> "${entry.setup.playerName}-${entry.finishedAt}" }) { index, entry ->
            RankingEntryCard(
                position = index + 1,
                entry = entry,
            )
        }
    }
}

@Preview
@Composable
private fun RankingListPreview() {
    val baseInstant = Instant.fromEpochSeconds(1_700_000_000)
    DynaquizTheme {
        RankingList(
            entries =
                persistentListOf(
                    RankingEntry(
                        setup =
                            QuizSetup(
                                playerName = "Leandro",
                                challengeMode = ChallengeMode.Timed.Hard,
                            ),
                        performance =
                            QuizPerformance(
                                score = Score(420),
                                correctAnswers = 9,
                                totalQuestions = 10,
                            ),
                        finishedAt = baseInstant,
                    ),
                    RankingEntry(
                        setup =
                            QuizSetup(
                                playerName = "Carla",
                                challengeMode = ChallengeMode.Timed.Medium,
                            ),
                        performance =
                            QuizPerformance(
                                score = Score(200),
                                correctAnswers = 8,
                                totalQuestions = 10,
                            ),
                        finishedAt = baseInstant,
                    ),
                    RankingEntry(
                        setup =
                            QuizSetup(
                                playerName = "Bruno",
                                challengeMode = ChallengeMode.Relaxed,
                            ),
                        performance =
                            QuizPerformance(
                                score = Score(7),
                                correctAnswers = 7,
                                totalQuestions = 10,
                            ),
                        finishedAt = baseInstant,
                    ),
                ),
        )
    }
}
