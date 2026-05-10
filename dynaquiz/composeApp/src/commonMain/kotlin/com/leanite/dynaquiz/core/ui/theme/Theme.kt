package com.leanite.dynaquiz.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DynaquizLightColorScheme = lightColorScheme(
    primary = DynamoxPurple,
    onPrimary = White,
    primaryContainer = DynamoxPurpleLight,
    onPrimaryContainer = White,
    background = White,
    onBackground = Color.Black,
    surface = OffWhite,
    onSurface = Color.Black,
)

@Composable
fun DynaquizTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DynaquizLightColorScheme,
        content = content,
    )
}