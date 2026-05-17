package com.leanite.dynaquiz.feature.splash

import app.cash.turbine.test
import com.leanite.dynaquiz.core.domain.repository.DatabaseRepository
import com.leanite.dynaquiz.core.domain.repository.QuizRepository
import com.leanite.dynaquiz.core.domain.usecase.WarmupDatabaseUseCase
import com.leanite.dynaquiz.core.domain.usecase.WarmupServerUseCase
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val quizRepository = mock<QuizRepository>(MockMode.autofill)
    private val databaseRepository = mock<DatabaseRepository>(MockMode.autofill)
    private val warmupServerUseCase = WarmupServerUseCase(quizRepository)
    private val warmupDatabaseUseCase = WarmupDatabaseUseCase(databaseRepository)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        everySuspend { quizRepository.warmupServer() } returns Unit
        everySuspend { databaseRepository.warmup() } returns Unit
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() =
        SplashViewModel(
            warmupServerUseCase = warmupServerUseCase,
            warmupDatabaseUseCase = warmupDatabaseUseCase,
        )

    @Test
    fun `init should trigger server warmup exactly once`() =
        runTest {
            createViewModel()

            verifySuspend { quizRepository.warmupServer() }
        }

    @Test
    fun `init should trigger database warmup exactly once`() =
        runTest {
            createViewModel()

            verifySuspend { databaseRepository.warmup() }
        }

    @Test
    fun `AnimationFinished should emit NavigateToNext`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.events.test {
                viewModel.onIntent(SplashIntent.AnimationFinished)

                assertEquals(SplashEvent.NavigateToNext, awaitItem())
            }
        }
}
