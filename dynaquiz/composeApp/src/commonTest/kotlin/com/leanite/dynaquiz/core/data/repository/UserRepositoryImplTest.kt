package com.leanite.dynaquiz.core.data.repository

import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserRepositoryImplTest {

    private val settings = MapSettings()

    private fun createRepository() = UserRepositoryImpl(settings = settings)

    @Test
    fun `getLastNickname should return null when no nickname has been saved`() = runTest {
        assertNull(createRepository().getLastNickname())
    }

    @Test
    fun `setLastNickname should persist value so a following getLastNickname returns it`() = runTest {
        val repository = createRepository()

        repository.setLastNickname("Leandro")

        assertEquals("Leandro", repository.getLastNickname())
    }

    @Test
    fun `setLastNickname should overwrite the previous value`() = runTest {
        val repository = createRepository()

        repository.setLastNickname("Leandro")
        repository.setLastNickname("Carla")

        assertEquals("Carla", repository.getLastNickname())
    }
}
