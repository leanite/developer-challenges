package com.leanite.dynaquiz.core.domain.model

import kotlin.time.Instant

data class RankingEntry(
    val playerName: String,
    val challengeMode: ChallengeMode,
    val score: Score,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val finishedAt: Instant,
)
