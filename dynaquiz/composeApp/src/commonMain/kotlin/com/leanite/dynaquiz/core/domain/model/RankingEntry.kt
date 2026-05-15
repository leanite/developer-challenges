package com.leanite.dynaquiz.core.domain.model

import androidx.compose.runtime.Immutable
import kotlin.time.Instant

@Immutable
data class RankingEntry(
    val playerName: String,
    val challengeMode: ChallengeMode,
    val score: Score,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val finishedAt: Instant,
)