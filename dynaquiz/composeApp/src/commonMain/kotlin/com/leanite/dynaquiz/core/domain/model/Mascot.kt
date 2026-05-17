package com.leanite.dynaquiz.core.domain.model

import androidx.compose.runtime.Immutable

enum class MascotMood {
    Relaxed,
    Noob,
    Normal,
    Expert,
}

@Immutable
data class Mascot(
    val mood: MascotMood,
)
