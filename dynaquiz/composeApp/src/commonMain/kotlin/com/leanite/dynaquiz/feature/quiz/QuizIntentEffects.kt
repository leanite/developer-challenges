package com.leanite.dynaquiz.feature.quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun QuizIntentEffects(onIntent: (QuizIntent) -> Unit) {
    LaunchedEffect(Unit) {
        onIntent(QuizIntent.Started)
    }
}