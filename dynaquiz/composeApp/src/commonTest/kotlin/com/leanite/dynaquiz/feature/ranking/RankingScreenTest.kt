package com.leanite.dynaquiz.feature.ranking

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.RankingEntry
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsSelected
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

@OptIn(ExperimentalTestApi::class)
class RankingScreenTest : UiTest() {

    private fun entry(name: String, score: Int) = RankingEntry(
        playerName = name,
        challengeMode = ChallengeMode.Timed.Easy,
        score = Score(score),
        correctAnswers = 5,
        totalQuestions = 10,
        finishedAt = Instant.fromEpochMilliseconds(1_700_000_000_000L),
    )

    @Test
    fun `should render the All tab as selected by default`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                RankingScreen(uiState = RankingUiState(), onIntent = {})
            }
        }

        assertTextIsSelected("Todos")
    }

    @Test
    fun `should render the entries list when uiState has entries`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                RankingScreen(
                    uiState = RankingUiState(
                        entries = listOf(entry("Leandro", 200), entry("Bruno", 100)),
                    ),
                    onIntent = {},
                )
            }
        }

        assertTextIsDisplayed("Leandro")
        assertTextIsDisplayed("Bruno")
    }

    @Test
    fun `should render All tab empty copy when entries is empty and tab is All`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                RankingScreen(
                    uiState = RankingUiState(selectedTab = RankingTab.All, entries = emptyList()),
                    onIntent = {},
                )
            }
        }

        assertTextIsDisplayed("Nenhum jogo registrado ainda")
    }

    @Test
    fun `should render Mine tab empty copy when entries is empty and tab is Mine`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                RankingScreen(
                    uiState = RankingUiState(selectedTab = RankingTab.Mine, entries = emptyList()),
                    onIntent = {},
                )
            }
        }

        assertTextIsDisplayed("Você ainda não jogou")
    }

    @Test
    fun `should not render empty state copy while isLoading is true`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                RankingScreen(
                    uiState = RankingUiState(
                        selectedTab = RankingTab.All,
                        entries = emptyList(),
                        isLoading = true,
                    ),
                    onIntent = {},
                )
            }
        }

        onNodeWithText("Nenhum jogo registrado ainda").assertDoesNotExist()
    }

    @Test
    fun `should emit TabSelected Mine when user taps the Mine tab`() = runComposeUiTest {
        val captured = mutableListOf<RankingIntent>()
        setContent {
            DynaquizTheme {
                RankingScreen(
                    uiState = RankingUiState(),
                    onIntent = { captured += it },
                )
            }
        }

        clickOnText("Meus jogos")

        assertEquals(1, captured.size)
        assertEquals(RankingIntent.TabSelected(RankingTab.Mine), captured.single())
    }
}
