package com.leanite.dynaquiz.core.ui.common

import androidx.compose.runtime.Composable
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.res.CoreRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChallengeMode.label(): String = stringResource(
    when (this) {
        ChallengeMode.Relaxed -> CoreRes.Strings.ModeRelaxed
        ChallengeMode.Timed.Easy -> CoreRes.Strings.ModeEasy
        ChallengeMode.Timed.Medium -> CoreRes.Strings.ModeMedium
        ChallengeMode.Timed.Hard -> CoreRes.Strings.ModeHard
    }
)