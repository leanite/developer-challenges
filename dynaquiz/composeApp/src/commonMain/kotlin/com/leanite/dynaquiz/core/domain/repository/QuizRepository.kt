package com.leanite.dynaquiz.core.domain.repository

import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.result.AppResult

interface QuizRepository {
    suspend fun getRandomQuestion(): AppResult<Question>
}