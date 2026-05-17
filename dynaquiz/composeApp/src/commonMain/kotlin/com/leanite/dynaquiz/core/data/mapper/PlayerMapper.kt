package com.leanite.dynaquiz.core.data.mapper

import com.leanite.dynaquiz.core.domain.model.Player
import com.leanite.dynaquiz.core.domain.model.PlayerId
import com.leanite.dynaquiz.database.PlayerEntity

internal fun PlayerEntity.toDomain(): Player =
    Player(
        id = PlayerId(id),
        name = name,
    )
