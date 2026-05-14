package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.Question

@Composable
fun QuestionCard(
    question: Question,
    selectedAnswer: String?,
    isSubmitting: Boolean,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Statement
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(20.dp),
                )
                .padding(20.dp),
        ) {
            Text(
                text = question.statement,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        // Options
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            question.options.forEach { option ->
                OptionButton(
                    text = option,
                    isSelected = selectedAnswer == option,
                    // Bloqueia novo tap após o primeiro
                    enabled = selectedAnswer == null && !isSubmitting,
                    onClick = { onOptionSelected(option) },
                )
            }
        }
    }
}