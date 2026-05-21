package com.leanite.dynaquiz.feature.difficulty

import app.cash.turbine.test
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.repository.ChallengeModeRepository
import com.leanite.dynaquiz.core.domain.usecase.GetLastChallengeModeUseCase
import com.leanite.dynaquiz.core.domain.usecase.SetLastChallengeModeUseCase
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DifficultyViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val repository = mock<ChallengeModeRepository>(MockMode.autofill)
    private val getLastChallengeMode = GetLastChallengeModeUseCase(repository)
    private val setLastChallengeMode = SetLastChallengeModeUseCase(repository)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { repository.getMode() } returns flowOf(ChallengeMode.Timed.Easy)
        everySuspend { repository.setMode(any()) } returns Unit
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() =
        DifficultyViewModel(
            getLastChallengeMode = getLastChallengeMode,
            setLastChallengeMode = setLastChallengeMode,
        )

    @Test
    fun `Load should reflect the last stored mode in uiState`() =
        runTest(testDispatcher) {
            every { repository.getMode() } returns flowOf(ChallengeMode.Timed.Hard)
            val viewModel = createViewModel()

            viewModel.onIntent(DifficultyIntent.Load)
            advanceUntilIdle()

            assertEquals(ChallengeMode.Timed.Hard, viewModel.uiState.value.selectedMode)
        }

    @Test
    fun `ModeSelected should update selectedMode in uiState without persisting`() =
        runTest(testDispatcher) {
            val viewModel = createViewModel()

            viewModel.onIntent(DifficultyIntent.ModeSelected(ChallengeMode.Relaxed))

            assertEquals(ChallengeMode.Relaxed, viewModel.uiState.value.selectedMode)
        }

    @Test
    fun `ConfirmClicked should persist the selected mode via SetLastChallengeMode`() =
        runTest(testDispatcher) {
            val viewModel = createViewModel()

            viewModel.onIntent(DifficultyIntent.ModeSelected(ChallengeMode.Timed.Medium))
            viewModel.onIntent(DifficultyIntent.ConfirmClicked)

            advanceUntilIdle()
            verifySuspend { repository.setMode(ChallengeMode.Timed.Medium) }
        }

    @Test
    fun `ConfirmClicked should emit NavigateBack after persisting`() =
        runTest(testDispatcher) {
            val viewModel = createViewModel()
            viewModel.onIntent(DifficultyIntent.ModeSelected(ChallengeMode.Timed.Hard))

            viewModel.events.test {
                viewModel.onIntent(DifficultyIntent.ConfirmClicked)

                assertEquals(DifficultyEvent.NavigateBack, awaitItem())
            }
        }

    @Test
    fun `ConfirmClicked should clear isConfirming after the flow finishes`() =
        runTest(testDispatcher) {
            val viewModel = createViewModel()

            viewModel.onIntent(DifficultyIntent.ConfirmClicked)

            assertEquals(false, viewModel.uiState.value.isConfirming)
        }

    @Test
    fun `BackClicked should emit NavigateBack`() =
        runTest(testDispatcher) {
            val viewModel = createViewModel()

            viewModel.events.test {
                viewModel.onIntent(DifficultyIntent.BackClicked)

                assertEquals(DifficultyEvent.NavigateBack, awaitItem())
            }
        }
}
