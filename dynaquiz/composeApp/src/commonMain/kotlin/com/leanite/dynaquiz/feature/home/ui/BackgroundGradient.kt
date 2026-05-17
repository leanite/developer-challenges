package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.leanite.dynaquiz.core.ui.theme.DynamoxPurple
import com.leanite.dynaquiz.core.ui.theme.DynamoxPurpleDeep
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme

@Composable
fun BackgroundGradient(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .background(
                    brush =
                        Brush.verticalGradient(
                            0f to DynamoxPurple,
                            0.40f to Color.Transparent,
                            0.60f to Color.Transparent,
                            1f to DynamoxPurpleDeep,
                        ),
                ),
    )
}

@Preview
@Composable
private fun BackgroundGradientPreview() {
    DynaquizTheme {
        BackgroundGradient(modifier = Modifier.fillMaxSize())
    }
}
