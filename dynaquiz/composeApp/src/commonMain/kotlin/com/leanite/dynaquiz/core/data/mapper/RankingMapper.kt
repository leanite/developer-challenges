package com.leanite.dynaquiz.core.data.mapper

import com.leanite.dynaquiz.core.domain.model.RankingEntry
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.database.SelectRanking
import com.leanite.dynaquiz.database.SelectRankingByPlayerName
import kotlin.time.Instant

internal fun SelectRanking.toDomain(): RankingEntry = RankingEntry(
    playerName = playerName,
    challengeMode = challengeMode,
    score = Score(scorePoints.toInt()),
    correctAnswers = correctCount.toInt(),
    totalQuestions = totalQuestions.toInt(),
    finishedAt = Instant.fromEpochMilliseconds(finishedAt),
)

internal fun SelectRankingByPlayerName.toDomain(): RankingEntry = RankingEntry(
    playerName = playerName,
    challengeMode = challengeMode,
    score = Score(scorePoints.toInt()),
    correctAnswers = correctCount.toInt(),
    totalQuestions = totalQuestions.toInt(),
    finishedAt = Instant.fromEpochMilliseconds(finishedAt),
)