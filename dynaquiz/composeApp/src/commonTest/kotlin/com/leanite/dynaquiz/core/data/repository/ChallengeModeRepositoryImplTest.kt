package com.leanite.dynaquiz.core.data.repository

import app.cash.turbine.test
import com.leanite.dynaquiz.core.data.datasource.ChallengeModeLocalDataSource
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ChallengeModeRepositoryImplTest {

    private val storedFlow = MutableStateFlow<String?>(null)

    private val dataSource = object : ChallengeModeLocalDataSource {
        override val storedMode = storedFlow
        var lastWritten: String? = null
        override suspend fun setStoredMode(value: String) {
            lastWritten = value
            storedFlow.value = value
        }
    }

    private fun createRepository() = ChallengeModeRepositoryImpl(dataSource = dataSource)

    @Test
    fun `getMode should emit Timed Easy as default when nothing is stored`() = runTest {
        createRepository().getMode().test {
            assertEquals(ChallengeMode.Timed.Easy, awaitItem())
        }
    }

    @Test
    fun `getMode should emit the mode corresponding to the stored serializedName`() = runTest {
        storedFlow.value = "HARD"

        createRepository().getMode().test {
            assertEquals(ChallengeMode.Timed.Hard, awaitItem())
        }
    }

    @Test
    fun `setMode should write the selected mode serializedName via the data source`() = runTest {
        createRepository().setMode(ChallengeMode.Timed.Medium)

        assertEquals("MEDIUM", dataSource.lastWritten)
    }

    @Test
    fun `setMode should make subsequent getMode emit the new value`() = runTest {
        val repository = createRepository()

        repository.setMode(ChallengeMode.Relaxed)

        repository.getMode().test {
            assertEquals(ChallengeMode.Relaxed, awaitItem())
        }
    }
}
