package com.leanite.dynaquiz.core.res

import androidx.compose.ui.unit.dp
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.core_mode_easy
import dynaquiz.composeapp.generated.resources.core_mode_hard
import dynaquiz.composeapp.generated.resources.core_mode_medium
import dynaquiz.composeapp.generated.resources.core_mode_relaxed

internal object CoreRes {

    object Strings {
        val ModeRelaxed = Res.string.core_mode_relaxed
        val ModeEasy = Res.string.core_mode_easy
        val ModeMedium = Res.string.core_mode_medium
        val ModeHard = Res.string.core_mode_hard
    }

    object Dimensions {
        val ButtonHeight = 56.dp
    }
}