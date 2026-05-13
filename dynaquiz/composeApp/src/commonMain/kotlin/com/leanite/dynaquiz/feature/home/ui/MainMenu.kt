package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.ui.common.GameButton
import com.leanite.dynaquiz.core.ui.common.GameButtonStyle
import com.leanite.dynaquiz.feature.home.res.HomeRes
import com.leanite.dynaquiz.feature.home.res.usableString
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.ic_button_difficulty
import dynaquiz.composeapp.generated.resources.ic_button_play
import dynaquiz.composeapp.generated.resources.ic_button_ranking
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainMenu(
    isStartEnabled: Boolean,
    onStartClick: () -> Unit,
    onDifficultyClick: () -> Unit,
    onRankingClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                shape = RoundedCornerShape(20.dp),
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GameButton(
            text = HomeRes.ButtonStart.usableString(),
            onClick = onStartClick,
            style = GameButtonStyle.Primary,
            enabled = isStartEnabled,
            leadingContent = {
                Image(
                    painter = painterResource(Res.drawable.ic_button_play),
                    contentDescription = null,
                    Modifier.size(36.dp)
                )
            }
        )

        GameButton(
            text = HomeRes.DifficultyTitle.usableString(),
            onClick = onDifficultyClick,
            style = GameButtonStyle.Secondary,
            leadingContent = {
                Image(
                    painter = painterResource(Res.drawable.ic_button_difficulty),
                    contentDescription = null,
                    Modifier.size(36.dp)
                )
            }
        )

        GameButton(
            text = HomeRes.RankingTitle.usableString(),
            onClick = onRankingClick,
            style = GameButtonStyle.Secondary,
            leadingContent = {
                Image(
                    painter = painterResource(Res.drawable.ic_button_ranking),
                    contentDescription = null,
                    Modifier.size(36.dp)
                )
            }
        )
    }
}