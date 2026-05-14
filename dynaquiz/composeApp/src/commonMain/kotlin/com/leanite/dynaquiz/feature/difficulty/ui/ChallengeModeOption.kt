package com.leanite.dynaquiz.feature.difficulty.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.ui.common.MascotAnimation
import com.leanite.dynaquiz.core.ui.common.SimpleSpriteAnimator
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.difficulty.res.DifficultyRes
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.anim_expert_1
import dynaquiz.composeapp.generated.resources.anim_expert_2
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChallengeModeOption(
    label: String,
    description: String,
    mode: ChallengeMode,
    isSelected: Boolean,
    onSelect: (ChallengeMode) -> Unit,
) {
    val primary = MaterialTheme.colorScheme.primary
    val containerColor = if (isSelected) primary else Color.White
    val contentColor = if (isSelected) Color.White else primary

    Surface(
        selected = isSelected,
        onClick = { onSelect(mode) },
        shape = RoundedCornerShape(CORNER_RADIUS),
        color = containerColor,
        contentColor = contentColor,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            MascotAnimation(
                mascotMood = mode.mascot.mood,
                shouldAnimate = isSelected
            )
            Column {
                Text(text = label, style = MaterialTheme.typography.headlineMedium)
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

private val CORNER_RADIUS = 20.dp

@Preview
@Composable
private fun ChallengeModeOptionPreview() {
    DynaquizTheme {
        ChallengeModeOption(
            label = stringResource(DifficultyRes.ModeRelaxedLabel),
            description = stringResource(DifficultyRes.ModeRelaxedDescription),
            mode = ChallengeMode.Relaxed,
            isSelected = true,
            onSelect = { },
        )
    }
}