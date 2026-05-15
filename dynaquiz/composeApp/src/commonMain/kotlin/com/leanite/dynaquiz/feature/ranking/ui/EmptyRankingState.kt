package com.leanite.dynaquiz.feature.ranking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.MascotMood
import com.leanite.dynaquiz.core.ui.common.MascotAnimation
import com.leanite.dynaquiz.feature.ranking.RankingTab
import com.leanite.dynaquiz.feature.ranking.res.RankingRes
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.anim_relaxed_1
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyRankingState(
    tab: RankingTab,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MascotAnimation(mascotMood = MascotMood.Relaxed)
        Text(
            text = stringResource(
                when (tab) {
                    RankingTab.All -> RankingRes.EmptyAll
                    RankingTab.Mine -> RankingRes.EmptyMine
                }
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}