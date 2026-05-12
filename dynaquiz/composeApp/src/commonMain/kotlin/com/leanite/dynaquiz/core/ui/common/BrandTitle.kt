package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.dynamox_logo
import org.jetbrains.compose.resources.painterResource

enum class BrandTitleSize {
    LARGE,
    NORMAL,
}

@Composable
fun BrandTitle(
    text: String,
    modifier: Modifier = Modifier,
    showCursor: Boolean = false,
    size: BrandTitleSize = BrandTitleSize.LARGE,
) {
    val spec = size.spec()
    val color = MaterialTheme.colorScheme.onPrimary

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SPACING),
    ) {
        Image(
            painter = painterResource(Res.drawable.dynamox_logo),
            contentDescription = "Dynamox logo",
            modifier = Modifier.height(spec.logoHeight),
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                color = color,
                style = spec.textStyle,
            )
            if (showCursor) {
                BlinkingCursor(height = spec.cursorHeight)
            }
        }
    }
}

@Immutable
private data class BrandTitleSpec(
    val logoHeight: Dp,
    val textStyle: TextStyle,
    val cursorHeight: Dp,
)

@Composable
private fun BrandTitleSize.spec(): BrandTitleSpec = when (this) {
    BrandTitleSize.LARGE -> BrandTitleSpec(
        logoHeight = 48.dp,
        textStyle = MaterialTheme.typography.displayMedium,
        cursorHeight = 40.dp,
    )
    BrandTitleSize.NORMAL -> BrandTitleSpec(
        logoHeight = 28.dp,
        textStyle = MaterialTheme.typography.titleLarge,
        cursorHeight = 22.dp,
    )
}

private val SPACING = 12.dp