package com.leanite.dynaquiz.core.domain.model

import androidx.compose.runtime.Immutable

enum class MascotMood {
    Relaxed,
    Noob,
    Normal,
    Expert,
}

@Immutable
data class Mascot(val mood: MascotMood)

fun ChallengeMode.toMascot(): Mascot = Mascot(mood = toMascotMood())

fun ChallengeMode.toMascotMood(): MascotMood = when (this) {
    ChallengeMode.Relaxed -> MascotMood.Relaxed
    ChallengeMode.Timed.Easy -> MascotMood.Noob
    ChallengeMode.Timed.Medium -> MascotMood.Normal
    ChallengeMode.Timed.Hard -> MascotMood.Expert
}