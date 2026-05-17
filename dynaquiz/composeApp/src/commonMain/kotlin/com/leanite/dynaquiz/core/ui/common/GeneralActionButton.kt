package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leanite.dynaquiz.core.res.CoreRes
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme

enum class GeneralActionButtonStyle { Primary, Secondary }

@Composable
fun GeneralActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GeneralActionButtonStyle = GeneralActionButtonStyle.Primary,
    enabled: Boolean = true,
) {
    val containerColor =
        when (style) {
            GeneralActionButtonStyle.Primary -> MaterialTheme.colorScheme.secondary
            GeneralActionButtonStyle.Secondary -> Color.White.copy(alpha = 0.95f)
        }
    val contentColor =
        when (style) {
            GeneralActionButtonStyle.Primary -> MaterialTheme.colorScheme.onSecondary
            GeneralActionButtonStyle.Secondary -> MaterialTheme.colorScheme.primary
        }
    val border =
        when (style) {
            GeneralActionButtonStyle.Primary -> BorderStroke(0.dp, MaterialTheme.colorScheme.secondary)
            GeneralActionButtonStyle.Secondary -> BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
        }

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = containerColor.copy(alpha = 0.5f),
                disabledContentColor = contentColor.copy(alpha = 0.5f),
            ),
        border = border,
        contentPadding = ButtonDefaults.ContentPadding,
        modifier =
            modifier
                .fillMaxWidth()
                .height(CoreRes.Dimensions.ButtonHeight),
    ) {
        Text(
            text = text,
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview
@Composable
fun GeneralActionButtonPrimaryPreview() {
    DynaquizTheme {
        GeneralActionButton(
            text = "SALVAR",
            onClick = {},
        )
    }
}

@Preview
@Composable
fun GeneralActionButtonSecondaryPreview() {
    DynaquizTheme {
        GeneralActionButton(
            text = "SALVAR",
            onClick = {},
            style = GeneralActionButtonStyle.Secondary,
        )
    }
}
