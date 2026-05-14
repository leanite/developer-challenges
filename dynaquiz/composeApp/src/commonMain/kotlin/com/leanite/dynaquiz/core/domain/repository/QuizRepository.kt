package com.leanite.dynaquiz.core.domain.repository

import com.leanite.dynaquiz.core.domain.model.Answer
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuestionId
import com.leanite.dynaquiz.core.domain.result.AppResult

interface QuizRepository {
    suspend fun getRandomQuestion(): AppResult<Question>
    suspend fun submitAnswer(questionId: QuestionId, answer: String): AppResult<Answer>

}