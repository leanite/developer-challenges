package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.MascotMood
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.anim_expert_1
import dynaquiz.composeapp.generated.resources.anim_expert_2
import dynaquiz.composeapp.generated.resources.anim_noob_1
import dynaquiz.composeapp.generated.resources.anim_noob_2
import dynaquiz.composeapp.generated.resources.anim_normal_1
import dynaquiz.composeapp.generated.resources.anim_normal_2
import dynaquiz.composeapp.generated.resources.anim_relaxed_1
import dynaquiz.composeapp.generated.resources.anim_relaxed_2
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun MascotAnimation(
    mascotMood: MascotMood,
    shouldAnimate: Boolean = true,
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
            idleSprite = Res.drawable.anim_noob_1
            activeSprite = Res.drawable.anim_noob_2
        }
        MascotMood.Normal -> {
            idleSprite = Res.drawable.anim_normal_1
            activeSprite = Res.drawable.anim_normal_2
        }
        MascotMood.Expert -> {
            idleSprite = Res.drawable.anim_expert_1
            activeSprite = Res.drawable.anim_expert_2
        }
    }

    SimpleSpriteAnimator(
        idle = idleSprite,
        active = activeSprite,
        shouldAnimate = shouldAnimate,
        modifier = modifier.size(SPRITE_SIZE)
    )
}

private val SPRITE_SIZE = 112.dp