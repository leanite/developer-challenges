package com.leanite.dynaquiz.feature.home

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HomeUiStateTest {
    @Test
    fun `canStart should be false when nickname is shorter than minimum length`() {
        val state = HomeUiState(nickname = "ab")

        assertFalse(state.canStart)
    }

    @Test
    fun `canStart should be false when nickname is only whitespace`() {
        val state = HomeUiState(nickname = "     ")

        assertFalse(state.canStart)
    }

    @Test
    fun `canStart should be true when trimmed nickname reaches minimum length`() {
        val state = HomeUiState(nickname = "  abc  ")

        assertTrue(state.canStart)
    }

    @Test
    fun `canStart should be false while isStarting is true`() {
        val state = HomeUiState(nickname = "Leandro", isStarting = true)

        assertFalse(state.canStart)
    }
}
