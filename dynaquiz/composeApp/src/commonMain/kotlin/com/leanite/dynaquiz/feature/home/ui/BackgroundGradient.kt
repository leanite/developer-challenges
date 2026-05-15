package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.leanite.dynaquiz.core.ui.theme.DynamoxPurple
import com.leanite.dynaquiz.core.ui.theme.DynamoxPurpleDeep

@Composable
fun BackgroundGradient(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    0f to DynamoxPurple,
                    0.40f to Color.Transparent,
                    0.60f to Color.Transparent,
                    1f to DynamoxPurpleDeep,
                ),
            ),
    )
}