package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode

@Composable
fun ChallengeModeOption(
    label: String,
    description: String,
    mode: ChallengeMode,
    isSelected: Boolean,
    onSelect: (ChallengeMode) -> Unit,
) {
    val primary = MaterialTheme.colorScheme.primary
    val containerColor = if (isSelected) primary else Color.White
    val contentColor = if (isSelected) Color.White else primary

    Surface(
        selected = isSelected,
        onClick = { onSelect(mode) },
        shape = RoundedCornerShape(CORNER_RADIUS),
        color = containerColor,
        contentColor = contentColor,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = label, style = MaterialTheme.typography.titleMedium)
            Text(text = description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private val CORNER_RADIUS = 20.dp