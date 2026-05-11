package com.leanite.dynaquiz.core.data.repository

import com.leanite.dynaquiz.core.data.datasource.QuizRemoteDataSource
import com.leanite.dynaquiz.core.data.error.toAppError
import com.leanite.dynaquiz.core.data.mapper.toDomain
import com.leanite.dynaquiz.core.domain.model.Question
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
                println("QuizRepo error: ${throwable::class.simpleName}: ${throwable.message}")
                throwable.printStackTrace()
                AppResult.Error(throwable.toAppError())
            }
        }
}