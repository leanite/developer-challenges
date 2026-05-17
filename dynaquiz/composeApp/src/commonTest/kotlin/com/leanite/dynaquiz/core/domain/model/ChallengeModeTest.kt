package com.leanite.dynaquiz.core.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ChallengeModeTest {

    @Test
    fun `fromSerializedName should map RELAXED to Relaxed`() {
        assertEquals(ChallengeMode.Relaxed, ChallengeMode.fromSerializedName("RELAXED"))
    }

    @Test
    fun `fromSerializedName should map EASY to Timed Easy`() {
        assertEquals(ChallengeMode.Timed.Easy, ChallengeMode.fromSerializedName("EASY"))
    }

    @Test
    fun `fromSerializedName should map MEDIUM to Timed Medium`() {
        assertEquals(ChallengeMode.Timed.Medium, ChallengeMode.fromSerializedName("MEDIUM"))
    }

    @Test
    fun `fromSerializedName should map HARD to Timed Hard`() {
        assertEquals(ChallengeMode.Timed.Hard, ChallengeMode.fromSerializedName("HARD"))
    }

    @Test
    fun `fromSerializedName with unknown value should fall back to Timed Easy`() {
        assertEquals(ChallengeMode.Timed.Easy, ChallengeMode.fromSerializedName("WHATEVER"))
    }

    @Test
    fun `each mode should declare its corresponding mascot mood`() {
        assertEquals(MascotMood.Relaxed, ChallengeMode.Relaxed.mascot.mood)
        assertEquals(MascotMood.Noob, ChallengeMode.Timed.Easy.mascot.mood)
        assertEquals(MascotMood.Normal, ChallengeMode.Timed.Medium.mascot.mood)
        assertEquals(MascotMood.Expert, ChallengeMode.Timed.Hard.mascot.mood)
    }

    @Test
    fun `Relaxed mode should have zero time bonus and one basePoint`() {
        assertEquals(1, ChallengeMode.Relaxed.basePoints)
        assertEquals(0, ChallengeMode.Relaxed.timeBonusPerSecond)
    }

    @Test
    fun `Hard mode should have the shortest perQuestionSeconds and the highest basePoints`() {
        assertEquals(10, ChallengeMode.Timed.Hard.perQuestionSeconds)
        assertEquals(8, ChallengeMode.Timed.Hard.basePoints)
        assertEquals(5, ChallengeMode.Timed.Hard.timeBonusPerSecond)
    }
}
