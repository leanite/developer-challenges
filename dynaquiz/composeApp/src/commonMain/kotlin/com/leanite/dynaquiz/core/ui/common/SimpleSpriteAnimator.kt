package com.leanite.dynaquiz.core.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    idleDuration: Duration = 1.seconds,
    activeDuration: Duration = 400.milliseconds,
    contentDescription: String? = null,
) {
    var showActive by remember { mutableStateOf(false) }

    LaunchedEffect(idle, active) {
        while (true) {
            delay(idleDuration)
            showActive = true
            delay(activeDuration)
            showActive = false
        }
    }

    Crossfade(
        targetState = showActive,
        animationSpec = snap(),
        modifier = modifier,
        label = "sprite-animator",
    ) { isActive ->
        Image(
            painter = painterResource(if (isActive) active else idle),
            contentDescription = contentDescription,
        )
    }
}