package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import kotlinx.coroutines.delay

@Composable
fun BlinkingCursor(
    height: Dp,
    modifier: Modifier = Modifier,
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(BLINK_INTERVAL_MS)
            visible = !visible
        }
    }

    Box(
        modifier = modifier
            .width(CURSOR_WIDTH)
            .height(height)
            .background(
                color = if (visible) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    Color.Transparent
                },
            ),
    )
}

private val CURSOR_WIDTH = 3.dp
private const val BLINK_INTERVAL_MS = 500L

@Preview
@Composable
private fun BlinkingCursorPreview() {
    DynaquizTheme {
        BlinkingCursor(height = 40.dp)
    }
}