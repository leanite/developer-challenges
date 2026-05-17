package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme

@Composable
fun CountdownDisplay(
    secondsRemaining: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = secondsRemaining,
            transitionSpec = {
                (
                    fadeIn(
                        tween(200),
                    ) +
                        scaleIn(
                            initialScale = 1.6f,
                            animationSpec = tween(400),
                        )
                ).togetherWith(
                    fadeOut(
                        tween(200),
                    ) +
                        scaleOut(
                            targetScale = 0.4f,
                            animationSpec = tween(400),
                        ),
                )
            },
            label = "countdown",
        ) { sec ->
            Text(
                text = sec.toString(),
                fontSize = 240.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Preview
@Composable
private fun CountdownDisplayPreview() {
    DynaquizTheme {
        CountdownDisplay(secondsRemaining = 3)
    }
}
