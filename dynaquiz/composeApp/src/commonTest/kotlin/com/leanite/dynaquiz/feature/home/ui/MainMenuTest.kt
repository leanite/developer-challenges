package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsNotEnabled
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class MainMenuTest : UiTest() {

    @Test
    fun `should render start difficulty and ranking buttons`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                MainMenu(
                    isStartEnabled = true,
                    onStartClick = {},
                    onDifficultyClick = {},
                    onRankingClick = {},
                )
            }
        }

        assertTextIsDisplayed("COMEÇAR QUIZ")
        assertTextIsDisplayed("DIFICULDADE")
        assertTextIsDisplayed("VER RANKING")
    }

    @Test
    fun `should disable start button when isStartEnabled is false`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                MainMenu(
                    isStartEnabled = false,
                    onStartClick = {},
                    onDifficultyClick = {},
                    onRankingClick = {},
                )
            }
        }

        assertTextIsNotEnabled("COMEÇAR QUIZ")
    }

    @Test
    fun `should emit onStartClick when user taps the start button`() = runComposeUiTest {
        var startClicks = 0
        setContent {
            DynaquizTheme {
                MainMenu(
                    isStartEnabled = true,
                    onStartClick = { startClicks++ },
                    onDifficultyClick = {},
                    onRankingClick = {},
                )
            }
        }

        clickOnText("COMEÇAR QUIZ")

        assertEquals(1, startClicks)
    }

    @Test
    fun `should emit onDifficultyClick when user taps the difficulty button`() = runComposeUiTest {
        var difficultyClicks = 0
        setContent {
            DynaquizTheme {
                MainMenu(
                    isStartEnabled = true,
                    onStartClick = {},
                    onDifficultyClick = { difficultyClicks++ },
                    onRankingClick = {},
                )
            }
        }

        clickOnText("DIFICULDADE")

        assertEquals(1, difficultyClicks)
    }

    @Test
    fun `should emit onRankingClick when user taps the ranking button`() = runComposeUiTest {
        var rankingClicks = 0
        setContent {
            DynaquizTheme {
                MainMenu(
                    isStartEnabled = true,
                    onStartClick = {},
                    onDifficultyClick = {},
                    onRankingClick = { rankingClicks++ },
                )
            }
        }

        clickOnText("VER RANKING")

        assertEquals(1, rankingClicks)
    }

    @Test
    fun `should not emit onStartClick when start is disabled and tapped`() = runComposeUiTest {
        var startClicks = 0
        setContent {
            DynaquizTheme {
                MainMenu(
                    isStartEnabled = false,
                    onStartClick = { startClicks++ },
                    onDifficultyClick = {},
                    onRankingClick = {},
                )
            }
        }

        runCatching { clickOnText("COMEÇAR QUIZ") }
        assertTrue(startClicks == 0)
    }
}
