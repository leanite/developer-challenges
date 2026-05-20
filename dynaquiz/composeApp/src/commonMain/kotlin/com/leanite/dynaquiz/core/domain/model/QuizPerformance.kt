package com.leanite.dynaquiz.core.domain.model

data class QuizPerformance(
    val score: Score,
    val correctAnswers: Int,
    val totalQuestions: Int,
)
