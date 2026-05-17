package com.leanite.dynaquiz.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leanite.dynaquiz.core.ui.common.BrandTitle
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.splash.anim.SplashAnimation

@Composable
fun SplashScreen(
    titleModifier: Modifier = Modifier,
    purpleSurfaceModifier: Modifier = Modifier,
    onAnimationFinished: () -> Unit
) {
    var displayedText by remember { mutableStateOf(SplashAnimation.INITIAL_TEXT) }
    var showCursor by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        SplashAnimation.runTypingSequence(
            onTextChanged = { displayedText = it },
        )

        // Cursor desaparece antes da transição
        showCursor = false

        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(purpleSurfaceModifier)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        BrandTitle(
            text = displayedText,
            showCursor = showCursor,
            modifier = titleModifier
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    DynaquizTheme {
        SplashScreen(onAnimationFinished = {})
    }
}