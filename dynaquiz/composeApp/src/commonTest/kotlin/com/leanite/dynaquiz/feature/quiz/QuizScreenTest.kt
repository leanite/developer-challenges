package com.leanite.dynaquiz.feature.quiz

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuestionId
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsNotEnabled
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class QuizScreenTest : UiTest() {
    private val sampleQuestion =
        Question(
            id = QuestionId("q-1"),
            statement = "Qual a capital do Brasil?",
            options = listOf("São Paulo", "Brasília", "Rio de Janeiro", "Salvador"),
        )

    private fun playingState(
        challengeMode: ChallengeMode = ChallengeMode.Relaxed,
        question: Question = sampleQuestion,
        selectedAnswer: String? = null,
        isSubmitting: Boolean = false,
        timeRemainingSec: Int? = null,
        showExitDialog: Boolean = false,
    ) = QuizUiState(
        challengeMode = challengeMode,
        phase =
            QuizPhase.Playing(
                question = question,
                selectedAnswer = selectedAnswer,
                isSubmitting = isSubmitting,
            ),
        timeRemainingSec = timeRemainingSec,
        showExitDialog = showExitDialog,
    )

    @Test
    fun `should render the countdown number when phase is Countdown`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    QuizScreen(
                        uiState =
                            QuizUiState(
                                challengeMode = ChallengeMode.Timed.Easy,
                                phase = QuizPhase.Countdown(countdownSecondsRemaining = 3),
                            ),
                        onIntent = {},
                    )
                }
            }

            assertTextIsDisplayed("3")
        }

    @Test
    fun `should render the question statement and options when phase is Playing`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    QuizScreen(uiState = playingState(), onIntent = {})
                }
            }

            assertTextIsDisplayed("Qual a capital do Brasil?")
            assertTextIsDisplayed("São Paulo")
            assertTextIsDisplayed("Brasília")
            assertTextIsDisplayed("Rio de Janeiro")
            assertTextIsDisplayed("Salvador")
        }

    @Test
    fun `should render the timer number when challengeMode is Timed and timeRemainingSec is set`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    QuizScreen(
                        uiState =
                            playingState(
                                challengeMode = ChallengeMode.Timed.Hard,
                                timeRemainingSec = 27,
                            ),
                        onIntent = {},
                    )
                }
            }

            assertTextIsDisplayed("27")
        }

    @Test
    fun `should not render the timer when challengeMode is Relaxed`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    QuizScreen(
                        uiState =
                            playingState(
                                challengeMode = ChallengeMode.Relaxed,
                                timeRemainingSec = null,
                            ),
                        onIntent = {},
                    )
                }
            }

            // 27 is not present in any visible text (sample question has no number)
            onNodeWithText("27").assertDoesNotExist()
        }

    @Test
    fun `should emit AnswerSelected with the tapped option text`() =
        runComposeUiTest {
            val captured = mutableListOf<QuizIntent>()
            setContent {
                DynaquizTheme {
                    QuizScreen(uiState = playingState(), onIntent = { captured += it })
                }
            }

            clickOnText("Brasília")

            assertTrue(captured.contains(QuizIntent.AnswerSelected("Brasília")))
        }

    @Test
    fun `should disable all options once selectedAnswer is set`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    QuizScreen(
                        uiState = playingState(selectedAnswer = "Brasília"),
                        onIntent = {},
                    )
                }
            }

            sampleQuestion.options.forEach { assertTextIsNotEnabled(it) }
        }

    @Test
    fun `should render the exit dialog when showExitDialog is true`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    QuizScreen(
                        uiState = playingState(showExitDialog = true),
                        onIntent = {},
                    )
                }
            }

            assertTextIsDisplayed("Opa! Vai mesmo?")
        }

    @Test
    fun `should not render the exit dialog when showExitDialog is false`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    QuizScreen(
                        uiState = playingState(showExitDialog = false),
                        onIntent = {},
                    )
                }
            }

            onNodeWithText("Opa! Vai mesmo?").assertDoesNotExist()
        }

    @Test
    fun `should emit ExitConfirmed when user taps the exit action in the dialog`() =
        runComposeUiTest {
            val captured = mutableListOf<QuizIntent>()
            setContent {
                DynaquizTheme {
                    QuizScreen(
                        uiState = playingState(showExitDialog = true),
                        onIntent = { captured += it },
                    )
                }
            }

            clickOnText("Sair")

            assertTrue(captured.contains(QuizIntent.ExitConfirmed))
        }

    @Test
    fun `should emit ExitCancelled when user taps the continue action in the dialog`() =
        runComposeUiTest {
            val captured = mutableListOf<QuizIntent>()
            setContent {
                DynaquizTheme {
                    QuizScreen(
                        uiState = playingState(showExitDialog = true),
                        onIntent = { captured += it },
                    )
                }
            }

            clickOnText("Continuar")

            assertTrue(captured.contains(QuizIntent.ExitCancelled))
        }
}
