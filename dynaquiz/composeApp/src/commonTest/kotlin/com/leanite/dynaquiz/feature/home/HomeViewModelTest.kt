package com.leanite.dynaquiz.feature.home

import app.cash.turbine.test
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.Player
import com.leanite.dynaquiz.core.domain.model.PlayerId
import com.leanite.dynaquiz.core.domain.repository.ChallengeModeRepository
import com.leanite.dynaquiz.core.domain.repository.PlayerRepository
import com.leanite.dynaquiz.core.domain.repository.UserRepository
import com.leanite.dynaquiz.core.domain.result.AppError
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.core.domain.usecase.GetLastChallengeModeUseCase
import com.leanite.dynaquiz.core.domain.usecase.GetLastNicknameUseCase
import com.leanite.dynaquiz.core.domain.usecase.RegisterOrFetchPlayerUseCase
import com.leanite.dynaquiz.core.domain.usecase.SetLastNicknameUseCase
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private val userRepository = mock<UserRepository>(MockMode.autofill)
    private val playerRepository = mock<PlayerRepository>(MockMode.autofill)
    private val challengeModeRepository = mock<ChallengeModeRepository>(MockMode.autofill)

    private val getLastNicknameUseCase = GetLastNicknameUseCase(userRepository)
    private val setLastNicknameUseCase = SetLastNicknameUseCase(userRepository)
    private val getLastChallengeModeUseCase = GetLastChallengeModeUseCase(challengeModeRepository)
    private val registerOrFetchPlayerUseCase = RegisterOrFetchPlayerUseCase(playerRepository)

    private val saveNicknameDelay = 200.milliseconds

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // Defaults: nothing stored, mode flow empty → uses default Easy
        everySuspend { userRepository.getLastNickname() } returns null
        everySuspend { userRepository.setLastNickname(any()) } returns Unit
        every { challengeModeRepository.getMode() } returns flowOf(ChallengeMode.Timed.Easy)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() =
        HomeViewModel(
            getLastNicknameUseCase = getLastNicknameUseCase,
            setLastNicknameUseCase = setLastNicknameUseCase,
            getLastChallengeModeUseCase = getLastChallengeModeUseCase,
            registerOrFetchPlayerUseCase = registerOrFetchPlayerUseCase,
            saveNicknameDelay = saveNicknameDelay,
        )

    private fun samplePlayer(name: String = "Leandro") =
        Player(
            id = PlayerId(1L),
            name = name,
        )

    @Test
    fun `Load should apply last nickname truncated to MAX_NICKNAME_LENGTH`() =
        runTest {
            val longNickname = "x".repeat(HomeValidation.MAX_NICKNAME_LENGTH + 5)
            everySuspend { userRepository.getLastNickname() } returns longNickname

            val viewModel = createViewModel()
            viewModel.onIntent(HomeIntent.Load)

            assertEquals(HomeValidation.MAX_NICKNAME_LENGTH, viewModel.uiState.value.nickname.length)
        }

    @Test
    fun `Load should apply empty string when getLastNickname returns null`() =
        runTest {
            everySuspend { userRepository.getLastNickname() } returns null

            val viewModel = createViewModel()
            viewModel.onIntent(HomeIntent.Load)

            assertEquals("", viewModel.uiState.value.nickname)
        }

    @Test
    fun `Load should observe stored challenge mode and reflect it in uiState`() =
        runTest {
            every { challengeModeRepository.getMode() } returns flowOf(ChallengeMode.Timed.Hard)

            val viewModel = createViewModel()
            viewModel.onIntent(HomeIntent.Load)

            assertEquals(ChallengeMode.Timed.Hard, viewModel.uiState.value.challengeMode)
        }

    @Test
    fun `Load should reflect later emissions of the challenge mode flow`() =
        runTest {
            val modeFlow = MutableStateFlow<ChallengeMode>(ChallengeMode.Timed.Easy)
            every { challengeModeRepository.getMode() } returns modeFlow

            val viewModel = createViewModel()
            viewModel.onIntent(HomeIntent.Load)
            assertEquals(ChallengeMode.Timed.Easy, viewModel.uiState.value.challengeMode)

            modeFlow.value = ChallengeMode.Timed.Hard

            assertEquals(ChallengeMode.Timed.Hard, viewModel.uiState.value.challengeMode)
        }

    @Test
    fun `NicknameChanged should update uiState nickname immediately`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.onIntent(HomeIntent.NicknameChanged("Lea"))

            assertEquals("Lea", viewModel.uiState.value.nickname)
        }

    @Test
    fun `NicknameChanged should truncate values longer than MAX_NICKNAME_LENGTH`() =
        runTest {
            val viewModel = createViewModel()
            val tooLong = "x".repeat(HomeValidation.MAX_NICKNAME_LENGTH + 10)

            viewModel.onIntent(HomeIntent.NicknameChanged(tooLong))

            assertEquals(HomeValidation.MAX_NICKNAME_LENGTH, viewModel.uiState.value.nickname.length)
        }

    @Test
    fun `repeated NicknameChanged should debounce and persist only the last value after the delay`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.onIntent(HomeIntent.NicknameChanged("Lea"))
            advanceTimeBy(saveNicknameDelay / 2)
            viewModel.onIntent(HomeIntent.NicknameChanged("Leandro"))
            advanceTimeBy(saveNicknameDelay + 50.milliseconds)

            verifySuspend { userRepository.setLastNickname("Leandro") }
        }

    @Test
    fun `StartQuizClicked with canStart false should be ignored`() =
        runTest {
            val viewModel = createViewModel()
            // nickname empty → canStart false

            viewModel.events.test {
                viewModel.onIntent(HomeIntent.StartQuizClicked)

                expectNoEvents()
                assertEquals(false, viewModel.uiState.value.isStarting)
            }
        }

    @Test
    fun `StartQuizClicked should trim the nickname before calling RegisterOrFetchPlayer`() =
        runTest {
            everySuspend { playerRepository.registerOrFetch(any()) } returns AppResult.Success(samplePlayer())
            val viewModel = createViewModel()
            viewModel.onIntent(HomeIntent.NicknameChanged("  Leandro  "))

            viewModel.onIntent(HomeIntent.StartQuizClicked)

            verifySuspend { playerRepository.registerOrFetch("Leandro") }
        }

    @Test
    fun `StartQuizClicked on success should emit NavigateToQuiz with the player name returned by the repository`() =
        runTest {
            everySuspend { playerRepository.registerOrFetch(any()) } returns
                AppResult.Success(samplePlayer(name = "Leandro"))
            val viewModel = createViewModel()
            viewModel.onIntent(HomeIntent.NicknameChanged("Leandro"))

            viewModel.events.test {
                viewModel.onIntent(HomeIntent.StartQuizClicked)

                val event = awaitItem()
                assertTrue(event is HomeEvent.NavigateToQuiz)
                assertEquals("Leandro", event.playerName)
                assertEquals(ChallengeMode.Timed.Easy, event.challengeMode)
            }
        }

    @Test
    fun `StartQuizClicked on error should emit ShowMessage PlayerSaveError with the error`() =
        runTest {
            everySuspend { playerRepository.registerOrFetch(any()) } returns AppResult.Error(AppError.NoInternet)
            val viewModel = createViewModel()
            viewModel.onIntent(HomeIntent.NicknameChanged("Leandro"))

            viewModel.events.test {
                viewModel.onIntent(HomeIntent.StartQuizClicked)

                val event = awaitItem()
                assertTrue(event is HomeEvent.ShowMessage)
                val message = event.type
                assertTrue(message is HomeMessage.PlayerSaveError)
                assertEquals(AppError.NoInternet, message.error)
            }
        }

    @Test
    fun `StartQuizClicked should clear isStarting after success`() =
        runTest {
            everySuspend { playerRepository.registerOrFetch(any()) } returns AppResult.Success(samplePlayer())
            val viewModel = createViewModel()
            viewModel.onIntent(HomeIntent.NicknameChanged("Leandro"))

            viewModel.onIntent(HomeIntent.StartQuizClicked)

            assertEquals(false, viewModel.uiState.value.isStarting)
        }

    @Test
    fun `StartQuizClicked should clear isStarting after error`() =
        runTest {
            everySuspend { playerRepository.registerOrFetch(any()) } returns AppResult.Error(AppError.Unknown)
            val viewModel = createViewModel()
            viewModel.onIntent(HomeIntent.NicknameChanged("Leandro"))

            viewModel.onIntent(HomeIntent.StartQuizClicked)

            assertEquals(false, viewModel.uiState.value.isStarting)
        }

    @Test
    fun `DifficultyClicked should emit NavigateToDifficulty`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.events.test {
                viewModel.onIntent(HomeIntent.DifficultyClicked)

                assertEquals(HomeEvent.NavigateToDifficulty, awaitItem())
            }
        }

    @Test
    fun `RankingClicked should emit NavigateToRanking with the current nickname`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.onIntent(HomeIntent.NicknameChanged("Leandro"))

            viewModel.events.test {
                viewModel.onIntent(HomeIntent.RankingClicked)

                val event = awaitItem()
                assertTrue(event is HomeEvent.NavigateToRanking)
                assertEquals("Leandro", event.playerName)
            }
        }
}
