package com.leanite.dynaquiz.navigation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
sealed interface QuizNavKey

@Immutable
@Serializable
data object Splash : QuizNavKey

@Immutable
@Serializable
data object Home : QuizNavKey

@Immutable
@Serializable
data object Difficulty : QuizNavKey