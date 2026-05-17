package com.leanite.dynaquiz.feature.quiz

import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.fixture.AnswerLogFixtures
import kotlin.test.Test
import kotlin.test.assertEquals

class QuizUiStateTest {

    @Test
    fun `questionNumber should be currentQuestionIndex plus one`() {
        val state = QuizUiState(
            challengeMode = ChallengeMode.Timed.Easy,
            currentQuestionIndex = 4,
        )

        assertEquals(5, state.questionNumber)
    }

    @Test
    fun `correctAnswers should count only logs with Confirmed correct true`() {
        val state = QuizUiState(
            challengeMode = ChallengeMode.Timed.Easy,
            answerLog = listOf(
                AnswerLogFixtures.confirmedCorrectNoTimeBonus,
                AnswerLogFixtures.confirmedCorrectNoTimeBonus,
                AnswerLogFixtures.confirmedIncorrect,
                AnswerLogFixtures.timedOut,
                AnswerLogFixtures.submitFailed,
            ),
        )

        assertEquals(2, state.correctAnswers)
    }

    @Test
    fun `correctAnswers should be zero when answerLog is empty`() {
        val state = QuizUiState(challengeMode = ChallengeMode.Relaxed)

        assertEquals(0, state.correctAnswers)
    }
}
