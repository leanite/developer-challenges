package com.leanite.dynaquiz.feature.ranking

import app.cash.turbine.test
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.QuizPerformance
import com.leanite.dynaquiz.core.domain.model.QuizSetup
import com.leanite.dynaquiz.core.domain.model.RankingEntry
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.core.domain.repository.RankingRepository
import com.leanite.dynaquiz.core.domain.result.AppError
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.core.domain.usecase.GetMyRankingUseCase
import com.leanite.dynaquiz.core.domain.usecase.GetRankingUseCase
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class RankingViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val rankingRepository = mock<RankingRepository>(MockMode.autofill)

    private val getRankingUseCase = GetRankingUseCase(rankingRepository)
    private val getMyRankingUseCase = GetMyRankingUseCase(rankingRepository)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(playerName: String = "Leandro") =
        RankingViewModel(
            playerName = playerName,
            getRankingUseCase = getRankingUseCase,
            getMyRankingUseCase = getMyRankingUseCase,
        )

    private fun entry(
        player: String = "Leandro",
        score: Int = 100,
    ) = RankingEntry(
        setup =
            QuizSetup(
                playerName = player,
                challengeMode = ChallengeMode.Timed.Easy,
            ),
        performance =
            QuizPerformance(
                score = Score(score),
                correctAnswers = 5,
                totalQuestions = 10,
            ),
        finishedAt = Instant.fromEpochMilliseconds(0L),
    )

    @Test
    fun `Load should default to All tab and call GetRanking`() =
        runTest(testDispatcher) {
            everySuspend { rankingRepository.getTopRanking() } returns AppResult.Success(emptyList())
            val viewModel = createViewModel()

            viewModel.onIntent(RankingIntent.Load)
            advanceUntilIdle()

            assertEquals(RankingTab.All, viewModel.uiState.value.selectedTab)
            verifySuspend { rankingRepository.getTopRanking() }
        }

    @Test
    fun `Load with Success should fill entries and clear isLoading`() =
        runTest(testDispatcher) {
            val entries = listOf(entry("A", 200), entry("B", 100))
            everySuspend { rankingRepository.getTopRanking() } returns AppResult.Success(entries)
            val viewModel = createViewModel()

            viewModel.onIntent(RankingIntent.Load)
            advanceUntilIdle()

            assertEquals(entries, viewModel.uiState.value.entries)
            assertEquals(false, viewModel.uiState.value.isLoading)
        }

    @Test
    fun `Load with Error should emit LoadFailed clear entries and clear isLoading`() =
        runTest(testDispatcher) {
            everySuspend { rankingRepository.getTopRanking() } returns AppResult.Error(AppError.NoInternet)
            val viewModel = createViewModel()

            viewModel.events.test {
                viewModel.onIntent(RankingIntent.Load)

                val event = awaitItem()
                assertTrue(event is RankingEvent.ShowMessage)
                assertEquals(RankingMessage.LoadFailed, event.type)
                assertEquals(emptyList(), viewModel.uiState.value.entries)
                assertEquals(false, viewModel.uiState.value.isLoading)
            }
        }

    @Test
    fun `TabSelected with the same tab should be ignored`() =
        runTest(testDispatcher) {
            everySuspend { rankingRepository.getTopRanking() } returns AppResult.Success(emptyList())

            val viewModel = createViewModel()
            viewModel.onIntent(RankingIntent.Load)
            advanceUntilIdle()
            viewModel.onIntent(RankingIntent.TabSelected(RankingTab.All))

            // initial Load + no extra call
            verifySuspend { rankingRepository.getTopRanking() }
        }

    @Test
    fun `TabSelected Mine should call GetMyRanking with the playerName from the constructor`() =
        runTest(testDispatcher) {
            everySuspend { rankingRepository.getTopRanking() } returns AppResult.Success(emptyList())
            everySuspend { rankingRepository.getTopRankingByPlayerName(any()) } returns AppResult.Success(emptyList())

            val viewModel = createViewModel(playerName = "Leandro")
            viewModel.onIntent(RankingIntent.Load)
            advanceUntilIdle()
            viewModel.onIntent(RankingIntent.TabSelected(RankingTab.Mine))
            advanceUntilIdle()

            assertEquals(RankingTab.Mine, viewModel.uiState.value.selectedTab)
            verifySuspend { rankingRepository.getTopRankingByPlayerName("Leandro") }
        }

    @Test
    fun `TabSelected to a different tab should update the tab and reload`() =
        runTest(testDispatcher) {
            everySuspend { rankingRepository.getTopRanking() } returns AppResult.Success(listOf(entry("A", 999)))
            val mineEntries = listOf(entry("Leandro", 50))
            everySuspend { rankingRepository.getTopRankingByPlayerName(any()) } returns AppResult.Success(mineEntries)

            val viewModel = createViewModel()
            viewModel.onIntent(RankingIntent.Load)
            advanceUntilIdle()
            viewModel.onIntent(RankingIntent.TabSelected(RankingTab.Mine))
            advanceUntilIdle()

            assertEquals(mineEntries, viewModel.uiState.value.entries)
        }

    @Test
    fun `BackPressed should emit NavigateBack`() =
        runTest(testDispatcher) {
            val viewModel = createViewModel()

            viewModel.events.test {
                viewModel.onIntent(RankingIntent.BackPressed)

                assertEquals(RankingEvent.NavigateBack, awaitItem())
            }
        }
}
