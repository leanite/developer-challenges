package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.Mascot
import com.leanite.dynaquiz.core.ui.common.MascotAnimation
import com.leanite.dynaquiz.core.ui.common.SimpleSpriteAnimator
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.anim_expert_1
import dynaquiz.composeapp.generated.resources.anim_expert_2
import dynaquiz.composeapp.generated.resources.anim_noob_1
import dynaquiz.composeapp.generated.resources.anim_noob_2
import dynaquiz.composeapp.generated.resources.anim_normal_1
import dynaquiz.composeapp.generated.resources.anim_normal_2
import dynaquiz.composeapp.generated.resources.anim_relaxed_1
import dynaquiz.composeapp.generated.resources.anim_relaxed_2

@Composable
fun ProfileCard(
    nickname: String,
    mascot: Mascot,
    onNicknameChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    0f to MaterialTheme.colorScheme.primary,
                    0.20f to Color.Transparent,
                    0.50f to Color.Transparent,
                    1f to MaterialTheme.colorScheme.tertiary.copy(alpha = 0.95f),
                ),
                shape = RoundedCornerShape(26.dp),
            )
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(18.dp),
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MascotAnimation(mascotMood = mascot.mood)

            Spacer(modifier = Modifier.width(16.dp))

            ProfileInput(
                nickname = nickname,
                onNicknameChange = onNicknameChange,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
