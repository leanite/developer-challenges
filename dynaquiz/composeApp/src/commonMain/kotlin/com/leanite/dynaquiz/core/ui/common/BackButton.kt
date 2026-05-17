package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
) {
    Row {
        Spacer(Modifier.width(16.dp))
        IconButton(
            onClick = onClick,
            modifier =
                modifier
                    .size(24.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null,
                colorFilter = ColorFilter.tint(tint),
            )
        }
        Spacer(Modifier.width(16.dp))
    }
}

@Preview
@Composable
fun BackButtonPreview() {
    DynaquizTheme {
        BackButton(onClick = {}, tint = Color.Black)
    }
}
