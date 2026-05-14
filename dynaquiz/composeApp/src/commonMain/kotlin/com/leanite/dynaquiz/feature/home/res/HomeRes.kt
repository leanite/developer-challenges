package com.leanite.dynaquiz.feature.home.res

import androidx.compose.runtime.Composable
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.home_button_difficulty
import dynaquiz.composeapp.generated.resources.home_button_ranking
import dynaquiz.composeapp.generated.resources.home_button_start
import dynaquiz.composeapp.generated.resources.home_profile_name_label
import dynaquiz.composeapp.generated.resources.home_profile_title
import dynaquiz.composeapp.generated.resources.home_save_error_generic
import dynaquiz.composeapp.generated.resources.home_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

internal object HomeRes {
    val Title: StringResource = Res.string.home_title
    val ProfileTitle: StringResource = Res.string.home_profile_title
    val ProfileNameLabel: StringResource = Res.string.home_profile_name_label
    val RankingTitle = Res.string.home_button_ranking
    val DifficultyTitle: StringResource = Res.string.home_button_difficulty
    val ButtonStart: StringResource = Res.string.home_button_start
    val SaveErrorGeneric: StringResource = Res.string.home_save_error_generic
}
