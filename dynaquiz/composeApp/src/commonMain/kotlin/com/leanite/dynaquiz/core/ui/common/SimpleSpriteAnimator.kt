package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.anim_normal_1
import dynaquiz.composeapp.generated.resources.anim_normal_2
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
fun SimpleSpriteAnimator(
    idle: DrawableResource,
    active: DrawableResource,
    modifier: Modifier = Modifier,
    shouldAnimate: Boolean = true,
    idleDuration: Duration = 1.seconds,
    activeDuration: Duration = 400.milliseconds,
    contentDescription: String? = null,
) {
    var showActive by remember { mutableStateOf(false) }

    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            while (true) {
                showActive = true
                delay(activeDuration)
                showActive = false
                delay(idleDuration)
            }
        } else {
            showActive = false
        }
    }

    Box(modifier = modifier) {
        Image(
            painter = painterResource(if (showActive) active else idle),
            contentDescription = contentDescription,
        )
    }
}

@Preview
@Composable
private fun SimpleSpriteAnimatorPreview() {
    DynaquizTheme {
        SimpleSpriteAnimator(
            idle = Res.drawable.anim_normal_1,
            active = Res.drawable.anim_normal_2,
            shouldAnimate = false,
        )
    }
}
