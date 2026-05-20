package com.leanite.dynaquiz.core.data.mapper

import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.database.SelectRanking
import com.leanite.dynaquiz.database.SelectRankingByPlayerName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class RankingMapperTest {
    @Test
    fun `SelectRanking toDomain should convert score and counts to Int wrapping score in Score`() {
        val row =
            SelectRanking(
                playerId = 1L,
                playerName = "Leandro",
                challengeMode = ChallengeMode.Timed.Hard,
                scorePoints = 420L,
                correctCount = 9L,
                totalQuestions = 10L,
                finishedAt = 1_700_000_000_000L,
            )

        val entry = row.toDomain()

        assertEquals("Leandro", entry.setup.playerName)
        assertEquals(ChallengeMode.Timed.Hard, entry.setup.challengeMode)
        assertEquals(Score(420), entry.performance.score)
        assertEquals(9, entry.performance.correctAnswers)
        assertEquals(10, entry.performance.totalQuestions)
    }

    @Test
    fun `SelectRanking toDomain should convert finishedAt epoch millis to Instant`() {
        val row =
            SelectRanking(
                playerId = 1L,
                playerName = "P",
                challengeMode = ChallengeMode.Relaxed,
                scorePoints = 0L,
                correctCount = 0L,
                totalQuestions = 0L,
                finishedAt = 1_700_000_000_000L,
            )

        assertEquals(
            Instant.fromEpochMilliseconds(1_700_000_000_000L),
            row.toDomain().finishedAt,
        )
    }

    @Test
    fun `SelectRankingByPlayerName toDomain should mirror the SelectRanking mapping`() {
        val row =
            SelectRankingByPlayerName(
                playerId = 2L,
                playerName = "Bruno",
                challengeMode = ChallengeMode.Timed.Medium,
                scorePoints = 100L,
                correctCount = 5L,
                totalQuestions = 10L,
                finishedAt = 1_700_000_000_000L,
            )

        val entry = row.toDomain()

        assertEquals("Bruno", entry.setup.playerName)
        assertEquals(ChallengeMode.Timed.Medium, entry.setup.challengeMode)
        assertEquals(Score(100), entry.performance.score)
        assertEquals(5, entry.performance.correctAnswers)
        assertEquals(10, entry.performance.totalQuestions)
        assertEquals(
            Instant.fromEpochMilliseconds(1_700_000_000_000L),
            entry.finishedAt,
        )
    }
}
