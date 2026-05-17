package com.leanite.dynaquiz.fixture

import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.database.PlayerEntity
import com.leanite.dynaquiz.database.SelectRanking
import com.leanite.dynaquiz.database.SelectRankingByPlayerName

internal object SelectRankingFixtures {

    val leandroHardTopRow: SelectRanking = SelectRanking(
        playerId = 1L,
        playerName = "Leandro",
        challengeMode = ChallengeMode.Timed.Hard,
        scorePoints = 420L,
        correctCount = 9L,
        totalQuestions = 10L,
        finishedAt = 1_700_000_000_000L,
    )

    val brunoRelaxedRow: SelectRanking = SelectRanking(
        playerId = 2L,
        playerName = "Bruno",
        challengeMode = ChallengeMode.Relaxed,
        scorePoints = 7L,
        correctCount = 7L,
        totalQuestions = 10L,
        finishedAt = 1_700_000_001_000L,
    )

    val leandroByNameRow: SelectRankingByPlayerName = SelectRankingByPlayerName(
        playerId = 1L,
        playerName = "Leandro",
        challengeMode = ChallengeMode.Timed.Medium,
        scorePoints = 200L,
        correctCount = 8L,
        totalQuestions = 10L,
        finishedAt = 1_700_000_002_000L,
    )

    fun playerEntity(
        id: Long = 1L,
        name: String = "Leandro",
        createdAt: Long = 1_700_000_000_000L,
    ): PlayerEntity = PlayerEntity(id = id, name = name, createdAt = createdAt)
}
