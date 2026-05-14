package com.leanite.dynaquiz.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ChallengeMode {
    val serializedName: String
    val mascot: Mascot

    data object Relaxed : ChallengeMode {
        override val serializedName: String = "RELAXED"
        override val mascot: Mascot = Mascot(MascotMood.Relaxed)
    }

    @Immutable
    sealed interface Timed : ChallengeMode {
        val perQuestionSeconds: Int

        data object Easy : Timed {
            override val perQuestionSeconds = 30
            override val serializedName: String = "EASY"
            override val mascot: Mascot = Mascot(MascotMood.Noob)
        }
        data object Medium : Timed {
            override val perQuestionSeconds = 20
            override val serializedName: String = "MEDIUM"
            override val mascot: Mascot = Mascot(MascotMood.Normal)
        }
        data object Hard : Timed {
            override val perQuestionSeconds = 10
            override val serializedName: String = "HARD"
            override val mascot: Mascot = Mascot(MascotMood.Expert)
        }
    }

    companion object {
        fun fromSerializedName(name: String): ChallengeMode? = when (name) {
            Relaxed.serializedName -> Relaxed
            Timed.Easy.serializedName -> Timed.Easy
            Timed.Medium.serializedName -> Timed.Medium
            Timed.Hard.serializedName -> Timed.Hard
            else -> null
        }
    }
}