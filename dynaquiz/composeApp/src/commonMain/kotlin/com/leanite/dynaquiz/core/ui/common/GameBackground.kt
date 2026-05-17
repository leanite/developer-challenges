package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.leanite.dynaquiz.core.ui.theme.DynamoxPurple
import com.leanite.dynaquiz.core.ui.theme.DynamoxPurpleDeep
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.bg_factory_night
import dynaquiz.composeapp.generated.resources.bg_factory_night_light
import org.jetbrains.compose.resources.painterResource

@Composable
fun GameBackground(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(DynamoxPurple)) {
        Image(
            painter = painterResource(Res.drawable.bg_factory_night_light),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun GameBackgroundPreview() {
    DynaquizTheme {
        GameBackground(modifier = Modifier.fillMaxSize())
    }
}