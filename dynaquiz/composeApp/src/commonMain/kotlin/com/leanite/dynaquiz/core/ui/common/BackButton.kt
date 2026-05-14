package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.Image
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Image(
            painter = painterResource(Res.drawable.ic_arrow_back),
            contentDescription = null,
            colorFilter = ColorFilter.tint(tint)
        )
    }
}