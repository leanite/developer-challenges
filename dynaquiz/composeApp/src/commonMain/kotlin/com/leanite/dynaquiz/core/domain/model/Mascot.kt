package com.leanite.dynaquiz.core.domain.model

enum class MascotMood {
    Relaxed,
    Noob,
    Normal,
    Expert,
}

data class Mascot(
    val mood: MascotMood,
)
