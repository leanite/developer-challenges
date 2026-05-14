package com.leanite.dynaquiz.core.data.repository

import com.leanite.dynaquiz.core.data.datasource.QuizRemoteDataSource
import com.leanite.dynaquiz.core.data.error.toAppError
import com.leanite.dynaquiz.core.data.mapper.toDomain
import com.leanite.dynaquiz.core.domain.model.AnswerResult
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuestionId
import com.leanite.dynaquiz.core.domain.repository.QuizRepository
import com.leanite.dynaquiz.core.domain.result.AppResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class QuizRepositoryImpl(
    private val remoteDataSource: QuizRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : QuizRepository {

    override suspend fun getRandomQuestion(): AppResult<Question> =
        withContext(ioDispatcher) {
            try {
                AppResult.Success(remoteDataSource.fetchRandomQuestion().toDomain())
            } catch (throwable: Throwable) {
                AppResult.Error(throwable.toAppError())
            }
        }

    override suspend fun submitAnswer(questionId: QuestionId, answer: String): AppResult<AnswerResult> =
        withContext(ioDispatcher) {
            try {
                AppResult.Success(remoteDataSource.submitAnswer(questionId.value, answer).toDomain())
            } catch (throwable: Throwable) {
                AppResult.Error(throwable.toAppError())
            }
        }
}