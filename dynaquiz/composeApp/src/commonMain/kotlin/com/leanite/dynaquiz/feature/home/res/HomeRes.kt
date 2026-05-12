package com.leanite.dynaquiz.feature.home.res

import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.home_button_start
import dynaquiz.composeapp.generated.resources.home_difficulty_title
import dynaquiz.composeapp.generated.resources.home_mode_easy_description
import dynaquiz.composeapp.generated.resources.home_mode_easy_label
import dynaquiz.composeapp.generated.resources.home_mode_hard_description
import dynaquiz.composeapp.generated.resources.home_mode_hard_label
import dynaquiz.composeapp.generated.resources.home_mode_medium_description
import dynaquiz.composeapp.generated.resources.home_mode_medium_label
import dynaquiz.composeapp.generated.resources.home_mode_relaxed_description
import dynaquiz.composeapp.generated.resources.home_mode_relaxed_label
import dynaquiz.composeapp.generated.resources.home_nickname_error_empty
import dynaquiz.composeapp.generated.resources.home_nickname_error_too_long
import dynaquiz.composeapp.generated.resources.home_nickname_error_too_short
import dynaquiz.composeapp.generated.resources.home_nickname_label
import dynaquiz.composeapp.generated.resources.home_ranking_description
import dynaquiz.composeapp.generated.resources.home_ranking_title
import dynaquiz.composeapp.generated.resources.home_save_error_generic
import dynaquiz.composeapp.generated.resources.home_title
import dynaquiz.composeapp.generated.resources.ic_chevron_forward
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

internal object HomeRes {
    val Title: StringResource = Res.string.home_title

    val NicknameLabel: StringResource = Res.string.home_nickname_label
    val NicknameErrorEmpty: StringResource = Res.string.home_nickname_error_empty
    val NicknameErrorTooShort: StringResource = Res.string.home_nickname_error_too_short
    val NicknameErrorTooLong: StringResource = Res.string.home_nickname_error_too_long
    val RankingTitle = Res.string.home_ranking_title
    val RankingDescription = Res.string.home_ranking_description

    val DifficultyTitle: StringResource = Res.string.home_difficulty_title

    val ModeRelaxedLabel: StringResource = Res.string.home_mode_relaxed_label
    val ModeRelaxedDescription: StringResource = Res.string.home_mode_relaxed_description
    val ModeEasyLabel: StringResource = Res.string.home_mode_easy_label
    val ModeEasyDescription: StringResource = Res.string.home_mode_easy_description
    val ModeMediumLabel: StringResource = Res.string.home_mode_medium_label
    val ModeMediumDescription: StringResource = Res.string.home_mode_medium_description
    val ModeHardLabel: StringResource = Res.string.home_mode_hard_label
    val ModeHardDescription: StringResource = Res.string.home_mode_hard_description

    val ButtonStart: StringResource = Res.string.home_button_start
    val SaveErrorGeneric: StringResource = Res.string.home_save_error_generic

    val Chevron: DrawableResource = Res.drawable.ic_chevron_forward
}