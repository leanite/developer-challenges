package com.leanite.dynaquiz.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class QuizSessionResult(
    val playerName: String,
    val challengeMode: ChallengeMode,
    val score: Score,
    val correctAnswers: Int,
    val totalQuestions: Int,
)