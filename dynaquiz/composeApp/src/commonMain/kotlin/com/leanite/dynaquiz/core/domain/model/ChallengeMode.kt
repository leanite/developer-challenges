package com.leanite.dynaquiz.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ChallengeMode {
    val serializedName: String
    val mascot: Mascot
    val basePoints: Int
    val timeBonusPerSecond: Int

    data object Relaxed : ChallengeMode {
        override val serializedName: String = "RELAXED"
        override val mascot: Mascot = Mascot(MascotMood.Relaxed)
        override val basePoints: Int = 1
        override val timeBonusPerSecond: Int = 0
    }

    @Immutable
    sealed interface Timed : ChallengeMode {
        val perQuestionSeconds: Int

        data object Easy : Timed {
            override val perQuestionSeconds = 30
            override val serializedName: String = "EASY"
            override val mascot: Mascot = Mascot(MascotMood.Noob)
            override val basePoints: Int = 2
            override val timeBonusPerSecond: Int = 1
        }
        data object Medium : Timed {
            override val perQuestionSeconds = 20
            override val serializedName: String = "MEDIUM"
            override val mascot: Mascot = Mascot(MascotMood.Normal)
            override val basePoints: Int = 4
            override val timeBonusPerSecond: Int = 2
        }
        data object Hard : Timed {
            override val perQuestionSeconds = 10
            override val serializedName: String = "HARD"
            override val mascot: Mascot = Mascot(MascotMood.Expert)
            override val basePoints: Int = 8
            override val timeBonusPerSecond: Int = 5
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