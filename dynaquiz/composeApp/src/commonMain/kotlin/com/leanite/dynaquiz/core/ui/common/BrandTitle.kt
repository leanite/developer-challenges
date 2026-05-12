package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.dynamox_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun BrandTitle(
    text: String,
    modifier: Modifier = Modifier,
    showCursor: Boolean = false,
    logoHeight: Dp = DEFAULT_LOGO_HEIGHT,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SPACING),
    ) {
        Image(
            painter = painterResource(Res.drawable.dynamox_logo),
            contentDescription = "Dynamox logo",
            modifier = Modifier.height(logoHeight),
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.displayMedium,
            )
            if (showCursor) {
                BlinkingCursor(height = CURSOR_HEIGHT)
            }
        }
    }
}

private val DEFAULT_LOGO_HEIGHT = 48.dp
private val SPACING = 12.dp
private val CURSOR_HEIGHT = 40.dp
