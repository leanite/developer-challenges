package com.leanite.dynaquiz.feature.quiz

import app.cash.turbine.test
import com.leanite.dynaquiz.core.domain.model.Answer
import com.leanite.dynaquiz.core.domain.model.AnswerOutcome
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuestionId
import com.leanite.dynaquiz.core.domain.model.QuizPerformance
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.QuizSetup
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.core.domain.repository.QuizRepository
import com.leanite.dynaquiz.core.domain.repository.RankingRepository
import com.leanite.dynaquiz.core.domain.result.AppError
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.core.domain.usecase.GetRandomQuestionUseCase
import com.leanite.dynaquiz.core.domain.usecase.SaveQuizSessionUseCase
import com.leanite.dynaquiz.core.domain.usecase.SubmitAnswerUseCase
import dev.mokkery.MockMode
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val quizRepository = mock<QuizRepository>(MockMode.autofill)
    private val rankingRepository = mock<RankingRepository>(MockMode.autofill)

    private val getRandomQuestionUseCase = GetRandomQuestionUseCase(quizRepository)
    private val submitAnswerUseCase = SubmitAnswerUseCase(quizRepository)
    private val saveQuizSessionUseCase = SaveQuizSessionUseCase(rankingRepository)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        everySuspend { rankingRepository.saveSession(any()) } returns AppResult.Success(Unit)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(challengeMode: ChallengeMode = ChallengeMode.Timed.Easy) =
        QuizViewModel(
            setup =
                QuizSetup(
                    playerName = "Leandro",
                    challengeMode = challengeMode,
                ),
            getRandomQuestionUseCase = getRandomQuestionUseCase,
            submitAnswerUseCase = submitAnswerUseCase,
            saveQuizSessionUseCase = saveQuizSessionUseCase,
            timer = QuizTimerController(),
        )

    private fun question(
        id: String = "q-1",
        statement: String = "Q?",
        options: List<String> = listOf("A", "B"),
    ) = Question(QuestionId(id), statement, options)

    private fun stubQuestionsSequentially(questions: List<Question>) {
        // Returns Success(q1), Success(q2), ... Once exhausted, repeats the last one.
        val responses = questions
        var index = 0
        everySuspend { quizRepository.getRandomQuestion() } calls {
            val q = responses.getOrElse(index) { responses.last() }
            index++
            AppResult.Success(q)
        }
    }

    private fun stubAlwaysCorrect() {
        everySuspend { quizRepository.submitAnswer(any(), any()) } returns
            AppResult.Success(Answer(correct = true))
    }

    // ----- Countdown -----

    @Test
    fun `Started should start countdown at 3 and decrement each second to one`() =
        runTest(testDispatcher) {
            // Use Relaxed to avoid timer side effects after countdown
            stubQuestionsSequentially(listOf(question()))
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)

            assertEquals(QuizPhase.Countdown(3), viewModel.uiState.value.phase)
            advanceTimeBy(1.seconds + 100.milliseconds)
            assertEquals(QuizPhase.Countdown(2), viewModel.uiState.value.phase)
            advanceTimeBy(1.seconds + 100.milliseconds)
            assertEquals(QuizPhase.Countdown(1), viewModel.uiState.value.phase)
        }

    @Test
    fun `Started with successful prefetch should transition to Playing with the first question`() =
        runTest(testDispatcher) {
            // Relaxed has no timer, so advanceUntilIdle does not consume the prefetch into the current phase
            val q = question(id = "q-42")
            stubQuestionsSequentially(listOf(q, question("q-43")))
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()

            val phase = viewModel.uiState.value.phase
            assertTrue(phase is QuizPhase.Playing)
            assertEquals(q, phase.question)
        }

    @Test
    fun `Started with failure on first question should emit QuestionLoadFailed and NavigateBack`() =
        runTest(testDispatcher) {
            everySuspend { quizRepository.getRandomQuestion() } returns AppResult.Error(AppError.NoInternet)
            val viewModel = createViewModel()

            viewModel.events.test {
                viewModel.onIntent(QuizIntent.Started)
                advanceUntilIdle()

                val first = awaitItem()
                assertTrue(first is QuizEvent.ShowMessage)
                assertEquals(QuizMessage.QuestionLoadFailed, first.type)
                assertEquals(QuizEvent.NavigateBack, awaitItem())
            }
        }

    // ----- Timer -----

    @Test
    fun `Playing in Timed mode should initialize timeRemainingSec with perQuestionSeconds`() =
        runTest(testDispatcher) {
            // 3 stubs: first question + prefetch + a safety extra
            stubQuestionsSequentially(listOf(question("q-1"), question("q-2"), question("q-3")))
            val viewModel = createViewModel(ChallengeMode.Timed.Hard)

            viewModel.onIntent(QuizIntent.Started)
            // Advance just enough to finish countdown (3s) + small slack, without consuming timer ticks
            advanceTimeBy(3.seconds + 100.milliseconds)

            assertEquals(ChallengeMode.Timed.Hard.perQuestionSeconds, viewModel.uiState.value.timeRemainingSec)
        }

    @Test
    fun `timer should decrement each second during Playing`() =
        runTest(testDispatcher) {
            stubQuestionsSequentially(listOf(question("q-1"), question("q-2"), question("q-3")))
            val viewModel = createViewModel(ChallengeMode.Timed.Hard)

            viewModel.onIntent(QuizIntent.Started)
            advanceTimeBy(3.seconds + 100.milliseconds)

            val initial = viewModel.uiState.value.timeRemainingSec!!

            advanceTimeBy(1.seconds)

            assertEquals(initial - 1, viewModel.uiState.value.timeRemainingSec)
        }

    @Test
    fun `timer reaching zero should register a TimedOut log and advance to the next question`() =
        runTest(testDispatcher) {
            val q1 = question(id = "q-1")
            val q2 = question(id = "q-2")
            // q-3 to satisfy the prefetch scheduled when Playing(q-2) begins
            stubQuestionsSequentially(listOf(q1, q2, question("q-3")))
            val viewModel = createViewModel(ChallengeMode.Timed.Hard)

            viewModel.onIntent(QuizIntent.Started)
            advanceTimeBy(3.seconds + 100.milliseconds)

            // Drain the full per-question timer of Hard mode (10s)
            advanceTimeBy(ChallengeMode.Timed.Hard.perQuestionSeconds.seconds + 100.milliseconds)

            val log = viewModel.uiState.value.answerLog
            assertEquals(1, log.size)
            assertEquals(QuestionId("q-1"), log.first().questionId)
            assertEquals(AnswerOutcome.TimedOut, log.first().outcome)
            assertEquals(1, viewModel.uiState.value.currentQuestionIndex)
            assertEquals(q2, (viewModel.uiState.value.phase as QuizPhase.Playing).question)
        }

    @Test
    fun `Playing in Relaxed mode should keep timeRemainingSec null`() =
        runTest {
            stubQuestionsSequentially(listOf(question(), question("q-2")))
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()

            assertNull(viewModel.uiState.value.timeRemainingSec)
        }

    // ----- Answer selection -----

    @Test
    fun `AnswerSelected should mark selectedAnswer and isSubmitting immediately`() =
        runTest(testDispatcher) {
            stubQuestionsSequentially(listOf(question(), question("q-2")))
            // Submit suspends indefinitely (we never resolve it for this test)
            everySuspend { quizRepository.submitAnswer(any(), any()) } returns
                AppResult.Success(Answer(correct = true))
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()
            viewModel.onIntent(QuizIntent.AnswerSelected("B"))

            // After the intent the state is updated synchronously inside markAnswerSelected
            val phase = viewModel.uiState.value.phase as QuizPhase.Playing
            assertEquals("B", phase.selectedAnswer)
            assertTrue(phase.isSubmitting)
        }

    @Test
    fun `AnswerSelected while isSubmitting should be ignored and not trigger a second submit`() =
        runTest(testDispatcher) {
            stubQuestionsSequentially(listOf(question(), question("q-2")))
            stubAlwaysCorrect()
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()

            viewModel.onIntent(QuizIntent.AnswerSelected("B"))
            // Second click before the submit resolves should be a no-op
            viewModel.onIntent(QuizIntent.AnswerSelected("C"))
            advanceUntilIdle()

            verifySuspend { quizRepository.submitAnswer(any(), "B") }
        }

    @Test
    fun `AnswerSelected with Success Confirmed correct should register log Confirmed correct true`() =
        runTest(testDispatcher) {
            stubQuestionsSequentially(listOf(question(), question("q-2")))
            stubAlwaysCorrect()
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()

            viewModel.onIntent(QuizIntent.AnswerSelected("A"))
            advanceUntilIdle()

            val lastLog =
                viewModel.uiState.value.answerLog
                    .last()
            assertEquals(AnswerOutcome.Confirmed(correct = true), lastLog.outcome)
        }

    @Test
    fun `AnswerSelected with Success Confirmed incorrect should register log Confirmed correct false`() =
        runTest(testDispatcher) {
            stubQuestionsSequentially(listOf(question(), question("q-2")))
            everySuspend { quizRepository.submitAnswer(any(), any()) } returns
                AppResult.Success(Answer(correct = false))
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()
            viewModel.onIntent(QuizIntent.AnswerSelected("A"))
            advanceUntilIdle()

            val lastLog =
                viewModel.uiState.value.answerLog
                    .last()
            assertEquals(AnswerOutcome.Confirmed(correct = false), lastLog.outcome)
        }

    @Test
    fun `AnswerSelected with Error should emit AnswerSubmitFailed and register outcome SubmitFailed`() =
        runTest(testDispatcher) {
            stubQuestionsSequentially(listOf(question(), question("q-2")))
            everySuspend { quizRepository.submitAnswer(any(), any()) } returns
                AppResult.Error(AppError.NoInternet)
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.events.test {
                viewModel.onIntent(QuizIntent.Started)
                advanceUntilIdle()
                viewModel.onIntent(QuizIntent.AnswerSelected("A"))
                advanceUntilIdle()

                val event = awaitItem()
                assertTrue(event is QuizEvent.ShowMessage)
                assertEquals(QuizMessage.AnswerSubmitFailed, event.type)
            }

            assertEquals(
                AnswerOutcome.SubmitFailed,
                viewModel.uiState.value.answerLog
                    .last()
                    .outcome,
            )
        }

    // ----- Prefetch -----

    @Test
    fun `entering Playing should prefetch the next question`() =
        runTest(testDispatcher) {
            stubQuestionsSequentially(listOf(question("q-1"), question("q-2")))
            stubAlwaysCorrect()
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()

            // First fetch (question 1) + prefetch (question 2)
            verifySuspend(VerifyMode.exactly(2)) { quizRepository.getRandomQuestion() }
        }

    @Test
    fun `prefetch should not be scheduled on the last question`() =
        runTest(testDispatcher) {
            val questions = (1..QuizRules.TOTAL_QUESTIONS).map { question("q-$it") }
            stubQuestionsSequentially(questions)
            stubAlwaysCorrect()
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()

            // Answer the first 9 to reach the 10th
            repeat(QuizRules.TOTAL_QUESTIONS - 1) {
                viewModel.onIntent(QuizIntent.AnswerSelected("A"))
                advanceUntilIdle()
            }

            // At this point all 10 questions have been fetched (1 initial + 9 prefetches).
            // Last question has no prefetch → total fetches must be exactly TOTAL_QUESTIONS.
            verifySuspend(VerifyMode.exactly(QuizRules.TOTAL_QUESTIONS)) {
                quizRepository.getRandomQuestion()
            }
        }

    // ----- Completion -----

    @Test
    fun `answering the tenth question should mark phase Completed and currentQuestionIndex equal to total`() =
        runTest(testDispatcher) {
            val questions = (1..QuizRules.TOTAL_QUESTIONS).map { question("q-$it") }
            stubQuestionsSequentially(questions)
            stubAlwaysCorrect()
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()
            repeat(QuizRules.TOTAL_QUESTIONS) {
                viewModel.onIntent(QuizIntent.AnswerSelected("A"))
                advanceUntilIdle()
            }

            assertEquals(QuizPhase.Completed, viewModel.uiState.value.phase)
            assertEquals(QuizRules.TOTAL_QUESTIONS, viewModel.uiState.value.currentQuestionIndex)
        }

    @Test
    fun `completion should call SaveQuizSessionUseCase with score computed from the answerLog`() =
        runTest(testDispatcher) {
            val questions = (1..QuizRules.TOTAL_QUESTIONS).map { question("q-$it") }
            stubQuestionsSequentially(questions)
            stubAlwaysCorrect()
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()
            repeat(QuizRules.TOTAL_QUESTIONS) {
                viewModel.onIntent(QuizIntent.AnswerSelected("A"))
                advanceUntilIdle()
            }

            // 10 corrects on Relaxed mode → basePoints 1 × 10 = 10
            verifySuspend {
                rankingRepository.saveSession(
                    QuizSessionResult(
                        setup =
                            QuizSetup(
                                playerName = "Leandro",
                                challengeMode = ChallengeMode.Relaxed,
                            ),
                        performance =
                            QuizPerformance(
                                score = Score(10),
                                correctAnswers = 10,
                                totalQuestions = 10,
                            ),
                    ),
                )
            }
        }

    @Test
    fun `completion should emit NavigateToResult with the QuizSessionResult`() =
        runTest {
            val questions = (1..QuizRules.TOTAL_QUESTIONS).map { question("q-$it") }
            stubQuestionsSequentially(questions)
            stubAlwaysCorrect()
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.events.test {
                viewModel.onIntent(QuizIntent.Started)
                advanceUntilIdle()
                repeat(QuizRules.TOTAL_QUESTIONS) {
                    viewModel.onIntent(QuizIntent.AnswerSelected("A"))
                    advanceUntilIdle()
                }

                val event = awaitItem()
                assertTrue(event is QuizEvent.NavigateToResult)
                assertEquals("Leandro", event.result.setup.playerName)
                assertEquals(10, event.result.performance.correctAnswers)
                assertEquals(Score(10), event.result.performance.score)
            }
        }

    // ----- Exit dialog -----

    @Test
    fun `BackPressed should open the exit dialog`() =
        runTest {
            stubQuestionsSequentially(listOf(question(), question("q-2")))
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()

            viewModel.onIntent(QuizIntent.BackPressed)

            assertTrue(viewModel.uiState.value.showExitDialog)
        }

    @Test
    fun `ExitCancelled should close the dialog keeping the running phase`() =
        runTest {
            stubQuestionsSequentially(listOf(question(), question("q-2")))
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()
            viewModel.onIntent(QuizIntent.BackPressed)

            viewModel.onIntent(QuizIntent.ExitCancelled)

            assertEquals(false, viewModel.uiState.value.showExitDialog)
            assertTrue(viewModel.uiState.value.phase is QuizPhase.Playing)
        }

    @Test
    fun `ExitConfirmed should close the dialog and emit NavigateBack`() =
        runTest {
            stubQuestionsSequentially(listOf(question(), question("q-2")))
            val viewModel = createViewModel(ChallengeMode.Relaxed)

            viewModel.onIntent(QuizIntent.Started)
            advanceUntilIdle()
            viewModel.onIntent(QuizIntent.BackPressed)

            viewModel.events.test {
                viewModel.onIntent(QuizIntent.ExitConfirmed)

                assertEquals(QuizEvent.NavigateBack, awaitItem())
                assertEquals(false, viewModel.uiState.value.showExitDialog)
            }
        }
}
