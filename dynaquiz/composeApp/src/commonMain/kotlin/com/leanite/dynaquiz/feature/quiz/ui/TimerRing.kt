package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme

@Composable
fun TimerRing(
    timeRemainingSec: Int,
    totalSec: Int,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    strokeWidth: Dp = 4.dp,
) {
    val targetProgress = (timeRemainingSec.toFloat() / totalSec.coerceAtLeast(1)).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 800),
        label = "timer-progress",
    )

    val isCritical = timeRemainingSec in 1..3
    val targetColor = if (isCritical) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
    val animatedColor by animateColorAsState(targetColor, label = "timer-color")

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            // Track de fundo
            drawArc(
                color = animatedColor.copy(alpha = 0.2f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke,
            )
            // Progresso
            drawArc(
                color = animatedColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = stroke,
            )
        }
        Text(
            text = timeRemainingSec.toString(),
            fontSize = (size/2).value.sp,
            fontWeight = FontWeight.SemiBold,
            color = animatedColor,
        )
    }
}

@Preview
@Composable
private fun TimeRingPreview() {
    DynaquizTheme {
        TimerRing(
            timeRemainingSec = 9,
            totalSec = 20
        )
    }
}