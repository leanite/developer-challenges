package com.leanite.dynaquiz.core.domain.model

import com.leanite.dynaquiz.fixture.AnswerLogFixtures
import kotlin.test.Test
import kotlin.test.assertEquals

class ScoreTest {
    @Test
    fun `plus should sum points of both Scores`() {
        val result = Score(10) + Score(15)

        assertEquals(Score(25), result)
    }

    @Test
    fun `scoreForCorrectAnswer in Relaxed should return only basePoints and ignore time remaining`() {
        val result = ChallengeMode.Relaxed.scoreForCorrectAnswer(timeRemainingSec = 30)

        assertEquals(Score(1), result)
    }

    @Test
    fun `scoreForCorrectAnswer in Timed Hard should apply basePoints plus time bonus per second`() {
        val result = ChallengeMode.Timed.Hard.scoreForCorrectAnswer(timeRemainingSec = 5)

        // basePoints 8 + (5 * timeBonus 5) = 33
        assertEquals(Score(33), result)
    }

    @Test
    fun `scoreForCorrectAnswer with negative time remaining should coerce to zero`() {
        val result = ChallengeMode.Timed.Easy.scoreForCorrectAnswer(timeRemainingSec = -10)

        // basePoints 2 + (0 * 1) = 2
        assertEquals(Score(2), result)
    }

    @Test
    fun `computeScore should sum points only from confirmed correct answers`() {
        val logs =
            listOf(
                AnswerLogFixtures.confirmedCorrectWithTimeRemaining(seconds = 10),
                AnswerLogFixtures.confirmedCorrectWithTimeRemaining(seconds = 5),
            )

        val result = logs.computeScore(ChallengeMode.Timed.Easy)

        // 2 logs * (basePoints 2 + bonus 1/s): 12 + 7 = 19
        assertEquals(Score(19), result)
    }

    @Test
    fun `computeScore should ignore TimedOut and SubmitFailed outcomes`() {
        val logs =
            listOf(
                AnswerLogFixtures.confirmedCorrectNoTimeBonus,
                AnswerLogFixtures.timedOut,
                AnswerLogFixtures.submitFailed,
            )

        val result = logs.computeScore(ChallengeMode.Relaxed)

        assertEquals(Score(1), result)
    }

    @Test
    fun `computeScore should ignore confirmed incorrect answers`() {
        val logs =
            listOf(
                AnswerLogFixtures.confirmedCorrectNoTimeBonus,
                AnswerLogFixtures.confirmedIncorrect,
            )

        val result = logs.computeScore(ChallengeMode.Relaxed)

        assertEquals(Score(1), result)
    }

    @Test
    fun `computeScore with empty list should return Score Zero`() {
        val result = emptyList<AnswerLog>().computeScore(ChallengeMode.Timed.Hard)

        assertEquals(Score.Zero, result)
    }

    @Test
    fun `compareTo should order Scores by points`() {
        val low = Score(5)
        val high = Score(20)

        assertEquals(true, low < high)
        assertEquals(true, high > low)
    }
}
