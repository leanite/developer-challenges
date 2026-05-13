package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.home.res.HomeRes
import com.leanite.dynaquiz.feature.home.res.usableString
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.ic_button_ranking
import org.jetbrains.compose.resources.painterResource

enum class GameButtonStyle { Primary, Secondary }

@Composable
fun GameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GameButtonStyle = GameButtonStyle.Primary,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
) {
    val containerColor = when (style) {
        GameButtonStyle.Primary -> MaterialTheme.colorScheme.secondary
        GameButtonStyle.Secondary -> Color.White.copy(alpha = 0.95f)
    }
    val contentColor = when (style) {
        GameButtonStyle.Primary -> MaterialTheme.colorScheme.onSecondary
        GameButtonStyle.Secondary -> MaterialTheme.colorScheme.primary
    }
    val border = when (style) {
        GameButtonStyle.Primary -> BorderStroke(2.dp, MaterialTheme.colorScheme.onSecondary)
        GameButtonStyle.Secondary -> BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f),
        ),
        border = border,
        contentPadding = ButtonDefaults.ContentPadding,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            if (leadingContent != null) {
                Box(
                    modifier = if (!enabled) {
                        Modifier.alpha(0.5f)
                    } else Modifier
                ) {
                    leadingContent.invoke()
                }
                Spacer(Modifier.width(16.dp))
            }
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun GameButtonPreview() {
    DynaquizTheme {
        GameButton(
            text = HomeRes.RankingTitle.usableString(),
            onClick = {},
            style = GameButtonStyle.Secondary,
            leadingContent = {
                Image(
                    painter = painterResource(Res.drawable.ic_button_ranking),
                    contentDescription = null,
                    Modifier.size(36.dp)
                )
            }
        )
    }
}