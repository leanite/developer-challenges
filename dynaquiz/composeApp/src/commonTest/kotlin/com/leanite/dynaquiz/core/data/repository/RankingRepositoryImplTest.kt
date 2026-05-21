package com.leanite.dynaquiz.core.data.repository

import com.leanite.dynaquiz.core.data.datasource.PlayerLocalDataSource
import com.leanite.dynaquiz.core.data.datasource.QuizSessionLocalDataSource
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.QuizPerformance
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.QuizSetup
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.core.domain.result.AppError
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.fixture.SelectRankingFixtures
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class RankingRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val quizSessionDataSource = mock<QuizSessionLocalDataSource>(MockMode.autofill)
    private val playerDataSource = mock<PlayerLocalDataSource>(MockMode.autofill)
    private val fixedNowMillis = 1_700_000_000_000L
    private val fixedClock =
        object : Clock {
            override fun now(): Instant = Instant.fromEpochMilliseconds(fixedNowMillis)
        }

    private fun createRepository() =
        RankingRepositoryImpl(
            quizSessionDataSource = quizSessionDataSource,
            playerDataSource = playerDataSource,
            clock = fixedClock,
            ioDispatcher = testDispatcher,
        )

    private fun sessionResult() =
        QuizSessionResult(
            setup =
                QuizSetup(
                    playerName = "Leandro",
                    challengeMode = ChallengeMode.Timed.Hard,
                ),
            performance =
                QuizPerformance(
                    score = Score(420),
                    correctAnswers = 9,
                    totalQuestions = 10,
                ),
        )

    @Test
    fun `saveSession should ensure player via findOrInsert before inserting the session`() =
        runTest(testDispatcher) {
            every { playerDataSource.findOrInsert(any(), any()) } returns
                SelectRankingFixtures.playerEntity(id = 1L, name = "Leandro")

            createRepository().saveSession(sessionResult())

            verify { playerDataSource.findOrInsert(name = "Leandro", createdAt = fixedNowMillis) }
        }

    @Test
    fun `saveSession should persist session fields using the playerId returned by findOrInsert`() =
        runTest(testDispatcher) {
            every { playerDataSource.findOrInsert(any(), any()) } returns
                SelectRankingFixtures.playerEntity(id = 42L, name = "Leandro")

            createRepository().saveSession(sessionResult())

            verify {
                quizSessionDataSource.insertSession(
                    playerId = 42L,
                    challengeMode = ChallengeMode.Timed.Hard,
                    scorePoints = 420L,
                    correctCount = 9L,
                    totalQuestions = 10L,
                    finishedAt = fixedNowMillis,
                )
            }
        }

    @Test
    fun `saveSession should return Success Unit on happy path`() =
        runTest(testDispatcher) {
            every { playerDataSource.findOrInsert(any(), any()) } returns
                SelectRankingFixtures.playerEntity()

            val result = createRepository().saveSession(sessionResult())

            assertEquals(AppResult.Success(Unit), result)
        }

    @Test
    fun `saveSession should return Error mapped from Throwable when findOrInsert throws`() =
        runTest(testDispatcher) {
            every { playerDataSource.findOrInsert(any(), any()) } throws IOException("db locked")

            val result = createRepository().saveSession(sessionResult())

            assertTrue(result is AppResult.Error)
            assertEquals(AppError.NoInternet, result.error)
        }

    @Test
    fun `saveSession should return Error mapped from Throwable when insertSession throws`() =
        runTest(testDispatcher) {
            every { playerDataSource.findOrInsert(any(), any()) } returns
                SelectRankingFixtures.playerEntity()
            every {
                quizSessionDataSource.insertSession(any(), any(), any(), any(), any(), any())
            } throws RuntimeException("disk full")

            val result = createRepository().saveSession(sessionResult())

            assertTrue(result is AppResult.Error)
            assertEquals(AppError.Unknown, result.error)
        }

    @Test
    fun `getTopRanking should map data source entries to domain RankingEntry list`() =
        runTest(testDispatcher) {
            every { quizSessionDataSource.selectRanking() } returns
                listOf(
                    SelectRankingFixtures.leandroHardTopRow,
                    SelectRankingFixtures.brunoRelaxedRow,
                )

            val result = createRepository().getTopRanking()

            assertTrue(result is AppResult.Success)
            assertEquals(2, result.data.size)
            assertEquals("Leandro", result.data[0].setup.playerName)
            assertEquals(Score(420), result.data[0].performance.score)
            assertEquals("Bruno", result.data[1].setup.playerName)
        }

    @Test
    fun `getTopRanking should return Success with empty list when there are no sessions`() =
        runTest(testDispatcher) {
            every { quizSessionDataSource.selectRanking() } returns emptyList()

            val result = createRepository().getTopRanking()

            assertEquals(AppResult.Success(emptyList()), result)
        }

    @Test
    fun `getTopRanking should return Error mapped from Throwable when data source throws`() =
        runTest(testDispatcher) {
            every { quizSessionDataSource.selectRanking() } throws RuntimeException("boom")

            val result = createRepository().getTopRanking()

            assertTrue(result is AppResult.Error)
            assertEquals(AppError.Unknown, result.error)
        }

    @Test
    fun `getTopRankingByPlayerName should forward player name to the data source`() =
        runTest(testDispatcher) {
            every { quizSessionDataSource.selectRankingByPlayerName(any()) } returns emptyList()

            createRepository().getTopRankingByPlayerName("Leandro")

            verify { quizSessionDataSource.selectRankingByPlayerName("Leandro") }
        }

    @Test
    fun `getTopRankingByPlayerName should map entries to domain RankingEntry list`() =
        runTest(testDispatcher) {
            every { quizSessionDataSource.selectRankingByPlayerName(any()) } returns
                listOf(
                    SelectRankingFixtures.leandroByNameRow,
                )

            val result = createRepository().getTopRankingByPlayerName("Leandro")

            assertTrue(result is AppResult.Success)
            assertEquals(1, result.data.size)
            assertEquals(
                Score(200),
                result.data
                    .single()
                    .performance.score,
            )
        }

    @Test
    fun `getTopRankingByPlayerName should return Error mapped from Throwable when data source throws`() =
        runTest(testDispatcher) {
            every { quizSessionDataSource.selectRankingByPlayerName(any()) } throws IOException("io")

            val result = createRepository().getTopRankingByPlayerName("Leandro")

            assertTrue(result is AppResult.Error)
            assertEquals(AppError.NoInternet, result.error)
        }
}
