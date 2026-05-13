package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.leanite.dynaquiz.core.domain.model.Mascot
import com.leanite.dynaquiz.core.domain.model.MascotMood
import dynaquiz.composeapp.generated.resources.Res //TODO: CommonRes?
import dynaquiz.composeapp.generated.resources.mascot_expert
import dynaquiz.composeapp.generated.resources.mascot_noob
import dynaquiz.composeapp.generated.resources.mascot_normal
import dynaquiz.composeapp.generated.resources.mascot_relaxed
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MascotImage(
    mascot: Mascot,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Mascote Dynaquiz",
) {
    Image(
        painter = painterResource(mascot.mood.drawable()),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

private fun MascotMood.drawable(): DrawableResource = when (this) {
    MascotMood.Relaxed -> Res.drawable.mascot_relaxed
    MascotMood.Noob -> Res.drawable.mascot_noob
    MascotMood.Normal -> Res.drawable.mascot_normal
    MascotMood.Expert -> Res.drawable.mascot_expert
}