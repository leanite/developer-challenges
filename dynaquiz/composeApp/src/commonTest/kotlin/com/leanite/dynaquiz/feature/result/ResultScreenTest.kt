package com.leanite.dynaquiz.feature.result

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextContainingIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ResultScreenTest : UiTest() {

    private val sampleResult = QuizSessionResult(
        playerName = "Leandro",
        challengeMode = ChallengeMode.Timed.Hard,
        score = Score(420),
        correctAnswers = 9,
        totalQuestions = 10,
    )

    @Test
    fun `should render the score points number`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                ResultScreen(uiState = ResultUiState(result = sampleResult), onIntent = {})
            }
        }

        assertTextIsDisplayed("420")
    }

    @Test
    fun `should render the points label`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                ResultScreen(uiState = ResultUiState(result = sampleResult), onIntent = {})
            }
        }

        assertTextIsDisplayed("PONTOS")
    }

    @Test
    fun `should render the player name in the congratulations message`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                ResultScreen(uiState = ResultUiState(result = sampleResult), onIntent = {})
            }
        }

        assertTextContainingIsDisplayed("Leandro")
    }

    @Test
    fun `should render correct answers slash total questions and mode label in details`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                ResultScreen(uiState = ResultUiState(result = sampleResult), onIntent = {})
            }
        }

        assertTextContainingIsDisplayed("9/10")
        assertTextContainingIsDisplayed("Difícil")
    }

    @Test
    fun `should emit RankingClicked when user taps the ranking button`() = runComposeUiTest {
        val captured = mutableListOf<ResultIntent>()
        setContent {
            DynaquizTheme {
                ResultScreen(
                    uiState = ResultUiState(result = sampleResult),
                    onIntent = { captured += it },
                )
            }
        }

        clickOnText("VER RANKING")

        assertEquals(1, captured.size)
        assertEquals(ResultIntent.RankingClicked, captured.single())
    }

    @Test
    fun `should emit HomeClicked when user taps the home button`() = runComposeUiTest {
        val captured = mutableListOf<ResultIntent>()
        setContent {
            DynaquizTheme {
                ResultScreen(
                    uiState = ResultUiState(result = sampleResult),
                    onIntent = { captured += it },
                )
            }
        }

        clickOnText("VOLTAR PARA HOME")

        assertEquals(1, captured.size)
        assertEquals(ResultIntent.HomeClicked, captured.single())
    }
}
