package com.leanite.dynaquiz.feature.ranking.res

import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.ranking_correct_format
import dynaquiz.composeapp.generated.resources.ranking_empty_all
import dynaquiz.composeapp.generated.resources.ranking_empty_mine
import dynaquiz.composeapp.generated.resources.ranking_medal_1
import dynaquiz.composeapp.generated.resources.ranking_medal_2
import dynaquiz.composeapp.generated.resources.ranking_medal_3
import dynaquiz.composeapp.generated.resources.ranking_msg_load_failed
import dynaquiz.composeapp.generated.resources.ranking_points_format
import dynaquiz.composeapp.generated.resources.ranking_tab_all
import dynaquiz.composeapp.generated.resources.ranking_tab_mine
import dynaquiz.composeapp.generated.resources.ranking_title

internal object RankingRes {
    val Title = Res.string.ranking_title
    val TabAll = Res.string.ranking_tab_all
    val TabMine = Res.string.ranking_tab_mine
    val EmptyAll = Res.string.ranking_empty_all
    val EmptyMine = Res.string.ranking_empty_mine
    val PointsFormat = Res.string.ranking_points_format
    val CorrectFormat = Res.string.ranking_correct_format
    val MsgLoadFailed = Res.string.ranking_msg_load_failed

    object Drawable {
        val Top1 = Res.drawable.ranking_medal_1
        val Top2 = Res.drawable.ranking_medal_2
        val Top3 = Res.drawable.ranking_medal_3
    }
}
