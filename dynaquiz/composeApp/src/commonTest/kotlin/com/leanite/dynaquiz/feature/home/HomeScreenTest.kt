package com.leanite.dynaquiz.feature.home

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsNotEnabled
import com.leanite.dynaquiz.uitest.clickOnText
import com.leanite.dynaquiz.uitest.typeOnField
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class HomeScreenTest : UiTest() {

    @Test
    fun `should render the nickname coming from uiState`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                HomeScreen(
                    uiState = HomeUiState(nickname = "Leandro"),
                    onIntent = {},
                )
            }
        }

        assertTextIsDisplayed("Leandro")
    }

    @Test
    fun `should render the start difficulty and ranking buttons`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                HomeScreen(uiState = HomeUiState(nickname = "Leandro"), onIntent = {})
            }
        }

        assertTextIsDisplayed("COMEÇAR QUIZ")
        assertTextIsDisplayed("DIFICULDADE")
        assertTextIsDisplayed("VER RANKING")
    }

    @Test
    fun `should disable the start button when canStart is false`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                HomeScreen(uiState = HomeUiState(nickname = "ab"), onIntent = {})
            }
        }

        assertTextIsNotEnabled("COMEÇAR QUIZ")
    }

    @Test
    fun `should disable the start button while isStarting is true`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                HomeScreen(
                    uiState = HomeUiState(nickname = "Leandro", isStarting = true),
                    onIntent = {},
                )
            }
        }

        assertTextIsNotEnabled("COMEÇAR QUIZ")
    }

    @Test
    fun `should emit NicknameChanged with trimmed uppercase value when user types`() = runComposeUiTest {
        val captured = mutableListOf<HomeIntent>()
        setContent {
            DynaquizTheme {
                HomeScreen(uiState = HomeUiState(), onIntent = { captured += it })
            }
        }

        typeOnField("NOME", "Lea")

        // HomeScreen applies it.trim().uppercase() before forwarding
        assertTrue(captured.contains(HomeIntent.NicknameChanged("LEA")))
    }

    @Test
    fun `should emit StartQuizClicked when user taps the start button`() = runComposeUiTest {
        val captured = mutableListOf<HomeIntent>()
        setContent {
            DynaquizTheme {
                HomeScreen(
                    uiState = HomeUiState(nickname = "Leandro"),
                    onIntent = { captured += it },
                )
            }
        }

        clickOnText("COMEÇAR QUIZ")

        assertTrue(captured.contains(HomeIntent.StartQuizClicked))
    }

    @Test
    fun `should emit DifficultyClicked when user taps the difficulty button`() = runComposeUiTest {
        val captured = mutableListOf<HomeIntent>()
        setContent {
            DynaquizTheme {
                HomeScreen(uiState = HomeUiState(), onIntent = { captured += it })
            }
        }

        clickOnText("DIFICULDADE")

        assertTrue(captured.contains(HomeIntent.DifficultyClicked))
    }

    @Test
    fun `should emit RankingClicked when user taps the ranking button`() = runComposeUiTest {
        val captured = mutableListOf<HomeIntent>()
        setContent {
            DynaquizTheme {
                HomeScreen(uiState = HomeUiState(), onIntent = { captured += it })
            }
        }

        clickOnText("VER RANKING")

        assertTrue(captured.contains(HomeIntent.RankingClicked))
    }

    @Test
    fun `should render character counter reflecting the current nickname length`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                HomeScreen(uiState = HomeUiState(nickname = "Leandro"), onIntent = {})
            }
        }

        assertTextIsDisplayed("7/${HomeValidation.MAX_NICKNAME_LENGTH} caracteres")
    }
}
