package com.leanite.dynaquiz.core.data.repository

import com.leanite.dynaquiz.core.data.datasource.QuizRemoteDataSource
import com.leanite.dynaquiz.core.data.model.AnswerResultDTO
import com.leanite.dynaquiz.core.data.model.QuestionDTO
import com.leanite.dynaquiz.core.domain.model.QuestionId
import com.leanite.dynaquiz.core.domain.result.AppError
import com.leanite.dynaquiz.core.domain.result.AppResult
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class QuizRepositoryImplTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val remoteDataSource = mock<QuizRemoteDataSource>(MockMode.autofill)

    private fun createRepository() =
        QuizRepositoryImpl(
            remoteDataSource = remoteDataSource,
            ioDispatcher = testDispatcher,
        )

    @Test
    fun `getRandomQuestion should return Success with mapped Question when remote responds`() =
        runTest {
            everySuspend { remoteDataSource.fetchRandomQuestion() } returns
                QuestionDTO(
                    id = "q-42",
                    statement = "Capital?",
                    options = listOf("A", "B", "C", "D"),
                )

            val result = createRepository().getRandomQuestion()

            assertTrue(result is AppResult.Success)
            assertEquals(QuestionId("q-42"), result.data.id)
            assertEquals("Capital?", result.data.statement)
            assertEquals(listOf("A", "B", "C", "D"), result.data.options)
        }

    @Test
    fun `getRandomQuestion should return Error NoInternet when remote throws IOException`() =
        runTest {
            everySuspend { remoteDataSource.fetchRandomQuestion() } throws IOException("offline")

            val result = createRepository().getRandomQuestion()

            assertTrue(result is AppResult.Error)
            assertEquals(AppError.NoInternet, result.error)
        }

    @Test
    fun `getRandomQuestion should return Error Unknown when remote throws unmapped Throwable`() =
        runTest {
            everySuspend { remoteDataSource.fetchRandomQuestion() } throws RuntimeException("boom")

            val result = createRepository().getRandomQuestion()

            assertTrue(result is AppResult.Error)
            assertEquals(AppError.Unknown, result.error)
        }

    @Test
    fun `submitAnswer should forward unwrapped questionId value and answer to remote`() =
        runTest {
            everySuspend { remoteDataSource.submitAnswer(any(), any()) } returns AnswerResultDTO(result = true)

            createRepository().submitAnswer(QuestionId("q-7"), answer = "B")

            verifySuspend { remoteDataSource.submitAnswer("q-7", "B") }
        }

    @Test
    fun `submitAnswer should return Success with mapped Answer when remote responds`() =
        runTest {
            everySuspend { remoteDataSource.submitAnswer(any(), any()) } returns AnswerResultDTO(result = true)

            val result = createRepository().submitAnswer(QuestionId("q-1"), "A")

            assertTrue(result is AppResult.Success)
            assertEquals(true, result.data.correct)
        }

    @Test
    fun `submitAnswer should return Error NoInternet when remote throws IOException`() =
        runTest {
            everySuspend { remoteDataSource.submitAnswer(any(), any()) } throws IOException("offline")

            val result = createRepository().submitAnswer(QuestionId("q-1"), "A")

            assertTrue(result is AppResult.Error)
            assertEquals(AppError.NoInternet, result.error)
        }

    @Test
    fun `warmupServer should swallow any Throwable without propagating`() =
        runTest {
            everySuspend { remoteDataSource.fetchRandomQuestion() } throws RuntimeException("boom")

            createRepository().warmupServer()
        }

    @Test
    fun `warmupServer should call fetchRandomQuestion exactly once`() =
        runTest {
            everySuspend { remoteDataSource.fetchRandomQuestion() } returns
                QuestionDTO(
                    id = "q-1",
                    statement = "",
                    options = emptyList(),
                )

            createRepository().warmupServer()

            verifySuspend { remoteDataSource.fetchRandomQuestion() }
        }
}
