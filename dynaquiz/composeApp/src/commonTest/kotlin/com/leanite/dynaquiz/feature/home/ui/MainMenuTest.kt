package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsNotEnabled
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MainMenuTest : UiTest() {
    @Composable
    private fun MainMenuContent(
        isStartEnabled: Boolean = true,
        onStartClick: () -> Unit = {},
        onDifficultyClick: () -> Unit = {},
        onRankingClick: () -> Unit = {},
    ) {
        DynaquizTheme {
            MainMenu(
                isStartEnabled = isStartEnabled,
                onStartClick = onStartClick,
                onDifficultyClick = onDifficultyClick,
                onRankingClick = onRankingClick,
            )
        }
    }

    @Test
    fun `should render start difficulty and ranking buttons`() =
        runComposeUiTest {
            setContent {
                MainMenuContent()
            }

            assertTextIsDisplayed("COMEÇAR QUIZ")
            assertTextIsDisplayed("DIFICULDADE")
            assertTextIsDisplayed("VER RANKING")
        }

    @Test
    fun `should disable start button when isStartEnabled is false`() =
        runComposeUiTest {
            setContent {
                MainMenuContent(
                    isStartEnabled = false,
                )
            }

            assertTextIsNotEnabled("COMEÇAR QUIZ")
        }

    @Test
    fun `should emit onStartClick when user taps the start button`() =
        runComposeUiTest {
            var startClicks = 0
            setContent {
                MainMenuContent(
                    onStartClick = { startClicks++ },
                )
            }

            clickOnText("COMEÇAR QUIZ")

            assertEquals(1, startClicks)
        }

    @Test
    fun `should emit onDifficultyClick when user taps the difficulty button`() =
        runComposeUiTest {
            var difficultyClicks = 0
            setContent {
                MainMenuContent(
                    onDifficultyClick = { difficultyClicks++ },
                )
            }

            clickOnText("DIFICULDADE")

            assertEquals(1, difficultyClicks)
        }

    @Test
    fun `should emit onRankingClick when user taps the ranking button`() =
        runComposeUiTest {
            var rankingClicks = 0
            setContent {
                MainMenuContent(
                    onRankingClick = { rankingClicks++ },
                )
            }

            clickOnText("VER RANKING")

            assertEquals(1, rankingClicks)
        }

    @Test
    fun `should not emit onStartClick when start is disabled and tapped`() =
        runComposeUiTest {
            var startClicks = 0
            setContent {
                MainMenuContent(
                    isStartEnabled = false,
                    onStartClick = { startClicks++ },
                )
            }

            runCatching { clickOnText("COMEÇAR QUIZ") }
            assertEquals(startClicks, 0)
        }
}
