package com.leanite.dynaquiz.core.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class Score(
    val points: Int,
) : Comparable<Score> {
    operator fun plus(other: Score): Score = Score(points + other.points)

    override fun compareTo(other: Score): Int = points.compareTo(other.points)

    companion object {
        val Zero: Score = Score(0)
    }
}

fun ChallengeMode.scoreForCorrectAnswer(timeRemainingSec: Int): Score =
    Score(basePoints + (timeRemainingSec.coerceAtLeast(0) * timeBonusPerSecond))

/**
 * Soma os pontos de todos os logs confirmados como corretos
 * Logs com TimedOut ou SubmitFailed não pontuam
 */
fun List<AnswerLog>.computeScore(mode: ChallengeMode): Score =
    fold(Score.Zero) { acc, log ->
        val confirmedCorrect = (log.outcome as? AnswerOutcome.Confirmed)?.correct == true

        if (confirmedCorrect) {
            acc + mode.scoreForCorrectAnswer(log.timeRemainingSec)
        } else {
            acc
        }
    }
