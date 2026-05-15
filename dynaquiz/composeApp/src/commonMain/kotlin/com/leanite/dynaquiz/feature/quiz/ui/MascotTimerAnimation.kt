package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.MascotMood
import com.leanite.dynaquiz.core.ui.common.SimpleSpriteAnimator
import com.leanite.dynaquiz.feature.quiz.res.QuizRes
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.anim_relaxed_1
import dynaquiz.composeapp.generated.resources.anim_relaxed_2
import dynaquiz.composeapp.generated.resources.anim_timer_expert_1
import dynaquiz.composeapp.generated.resources.anim_timer_expert_2
import dynaquiz.composeapp.generated.resources.anim_timer_noob_1
import dynaquiz.composeapp.generated.resources.anim_timer_noob_2
import dynaquiz.composeapp.generated.resources.anim_timer_normal_1
import dynaquiz.composeapp.generated.resources.anim_timer_normal_2
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun MascotTimerAnimation(
    mascotMood: MascotMood,
    modifier: Modifier = Modifier
) {
    val idleSprite: DrawableResource
    val activeSprite: DrawableResource

    when(mascotMood) {
        MascotMood.Relaxed -> {
            idleSprite = Res.drawable.anim_relaxed_1
            activeSprite = Res.drawable.anim_relaxed_2
        }
        MascotMood.Noob -> {
            idleSprite = Res.drawable.anim_timer_noob_1
            activeSprite = Res.drawable.anim_timer_noob_2
        }
        MascotMood.Normal -> {
            idleSprite = Res.drawable.anim_timer_normal_1
            activeSprite = Res.drawable.anim_timer_normal_2
        }
        MascotMood.Expert -> {
            idleSprite = Res.drawable.anim_timer_expert_1
            activeSprite = Res.drawable.anim_timer_expert_2
        }
    }

    SimpleSpriteAnimator(
        idle = idleSprite,
        active = activeSprite,
        modifier = modifier.size(SPRITE_SIZE)
    )
}

private val SPRITE_SIZE = QuizRes.Dimensions.TimerHeight