package com.leanite.dynaquiz.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GameColorScheme =
    darkColorScheme(
        primary = DynamoxPurple,
        onPrimary = Color.White,
        primaryContainer = DynamoxPurpleSurface,
        onPrimaryContainer = Color.White,
        secondary = MascotYellow,
        onSecondary = MascotBrown,
        tertiary = MascotYellowDeep,
        onTertiary = MascotBrown,
        background = DynamoxPurpleDeep,
        onBackground = Color.White,
        surface = DynamoxPurpleSurface,
        onSurface = Color.White,
        surfaceVariant = DynamoxPurpleSurface,
        onSurfaceVariant = Color.White.copy(alpha = 0.7f),
        error = FactoryRed,
        onError = Color.White,
    )

@Composable
fun DynaquizTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GameColorScheme,
        content = content,
    )
}
