package com.leanite.dynaquiz.core.domain.model

import kotlin.time.Instant

data class RankingEntry(
    val setup: QuizSetup,
    val performance: QuizPerformance,
    val finishedAt: Instant,
)
