package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme

@Composable
fun OptionButton(
    text: String,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.White.copy(alpha = 0.95f)
    }
    val contentColor = if (isSelected) {
        Color.White
    } else {
        MaterialTheme.colorScheme.primary
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        selected = isSelected,
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        )
    }
}

@Preview
@Composable
private fun OptionButtonUnselectedPreview() {
    DynaquizTheme {
        OptionButton(
            text = "Resposta A",
            isSelected = false,
            enabled = true,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun OptionButtonSelectedPreview() {
    DynaquizTheme {
        OptionButton(
            text = "Resposta A",
            isSelected = true,
            enabled = false,
            onClick = {},
        )
    }
}