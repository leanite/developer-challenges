package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.leanite.dynaquiz.feature.quiz.res.QuizRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExitQuizDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(QuizRes.DialogExitTitle)) },
        text = { Text(stringResource(QuizRes.DialogExitBody)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(QuizRes.ButtonExit))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(QuizRes.ButtonContinue))
            }
        },
    )
}