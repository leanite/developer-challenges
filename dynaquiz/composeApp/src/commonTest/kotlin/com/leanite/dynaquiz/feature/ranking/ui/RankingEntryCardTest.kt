package com.leanite.dynaquiz.feature.ranking.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.RankingEntry
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextContainingIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import kotlin.test.Test
import kotlin.time.Instant

@OptIn(ExperimentalTestApi::class)
class RankingEntryCardTest : UiTest() {

    private fun entry(player: String = "Leandro", scorePoints: Int = 200) = RankingEntry(
        playerName = player,
        challengeMode = ChallengeMode.Timed.Hard,
        score = Score(scorePoints),
        correctAnswers = 8,
        totalQuestions = 10,
        finishedAt = Instant.fromEpochMilliseconds(1_700_000_000_000L),
    )

    @Test
    fun `should render player name`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                RankingEntryCard(position = 1, entry = entry("Leandro"))
            }
        }

        assertTextIsDisplayed("Leandro")
    }

    @Test
    fun `should render score using PointsFormat`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                RankingEntryCard(position = 5, entry = entry(scorePoints = 420))
            }
        }

        assertTextIsDisplayed("420 pts")
    }

    @Test
    fun `should render correct count using CorrectFormat and the mode label`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                RankingEntryCard(position = 5, entry = entry())
            }
        }

        assertTextContainingIsDisplayed("Difícil")
        assertTextContainingIsDisplayed("8/10 acertos")
    }

    @Test
    fun `should render numeric position badge for positions beyond top three`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                RankingEntryCard(position = 12, entry = entry())
            }
        }

        assertTextIsDisplayed("12")
    }
}
