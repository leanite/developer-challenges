package com.leanite.dynaquiz.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ChallengeMode {
    val serializedName: String

    data object Relaxed : ChallengeMode {
        override val serializedName: String = "RELAXED"
    }

    @Immutable
    sealed interface Timed : ChallengeMode {
        val perQuestionSeconds: Int

        data object Easy : Timed {
            override val perQuestionSeconds = 30
            override val serializedName: String = "EASY"
        }
        data object Medium : Timed {
            override val perQuestionSeconds = 20
            override val serializedName: String = "MEDIUM"
        }
        data object Hard : Timed {
            override val perQuestionSeconds = 10
            override val serializedName: String = "HARD"
        }
    }

    companion object {
        fun fromSerializedName(name: String): ChallengeMode = when (name) {
            Relaxed.serializedName -> Relaxed
            Timed.Easy.serializedName -> Timed.Easy
            Timed.Medium.serializedName -> Timed.Medium
            Timed.Hard.serializedName -> Timed.Hard
            else -> error("Unknown ChallengeMode serializedName: '$name'")
        }
    }
}