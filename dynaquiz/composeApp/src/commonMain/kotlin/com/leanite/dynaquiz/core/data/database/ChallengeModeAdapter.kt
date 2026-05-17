package com.leanite.dynaquiz.core.data.database

import app.cash.sqldelight.ColumnAdapter
import com.leanite.dynaquiz.core.domain.model.ChallengeMode

internal val ChallengeModeAdapter: ColumnAdapter<ChallengeMode, String> =
    object : ColumnAdapter<ChallengeMode, String> {
        override fun decode(databaseValue: String): ChallengeMode =
            ChallengeMode.fromSerializedName(databaseValue)
                ?: error("Unknown ChallengeMode in database: '$databaseValue'")

        override fun encode(value: ChallengeMode): String = value.serializedName
    }
