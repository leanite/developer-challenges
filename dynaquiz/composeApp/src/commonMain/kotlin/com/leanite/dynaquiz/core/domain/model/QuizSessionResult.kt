package com.leanite.dynaquiz.core.domain.model

data class QuizSessionResult(
    val playerName: String,
    val challengeMode: ChallengeMode,
    val score: Score,
    val correctAnswers: Int,
    val totalQuestions: Int,
)
