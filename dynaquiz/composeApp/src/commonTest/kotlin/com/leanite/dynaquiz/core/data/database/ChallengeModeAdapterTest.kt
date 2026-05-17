package com.leanite.dynaquiz.core.data.database

import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import kotlin.test.Test
import kotlin.test.assertEquals

class ChallengeModeAdapterTest {

    @Test
    fun `encode should return the serializedName of each mode`() {
        assertEquals("RELAXED", ChallengeModeAdapter.encode(ChallengeMode.Relaxed))
        assertEquals("EASY", ChallengeModeAdapter.encode(ChallengeMode.Timed.Easy))
        assertEquals("MEDIUM", ChallengeModeAdapter.encode(ChallengeMode.Timed.Medium))
        assertEquals("HARD", ChallengeModeAdapter.encode(ChallengeMode.Timed.Hard))
    }

    @Test
    fun `decode should recover each mode from its serializedName`() {
        assertEquals(ChallengeMode.Relaxed, ChallengeModeAdapter.decode("RELAXED"))
        assertEquals(ChallengeMode.Timed.Easy, ChallengeModeAdapter.decode("EASY"))
        assertEquals(ChallengeMode.Timed.Medium, ChallengeModeAdapter.decode("MEDIUM"))
        assertEquals(ChallengeMode.Timed.Hard, ChallengeModeAdapter.decode("HARD"))
    }

    @Test
    fun `decode roundtrip with encode should return the original mode`() {
        val modes = listOf(
            ChallengeMode.Relaxed,
            ChallengeMode.Timed.Easy,
            ChallengeMode.Timed.Medium,
            ChallengeMode.Timed.Hard,
        )

        modes.forEach { mode ->
            assertEquals(mode, ChallengeModeAdapter.decode(ChallengeModeAdapter.encode(mode)))
        }
    }

    @Test
    fun `decode with unknown value should fall back to Timed Easy mirroring fromSerializedName`() {
        assertEquals(ChallengeMode.Timed.Easy, ChallengeModeAdapter.decode("WHATEVER"))
    }
}
