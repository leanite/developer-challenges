package com.leanite.dynaquiz.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leanite.dynaquiz.feature.home.HomeHost
import com.leanite.dynaquiz.feature.splash.SplashHost

@Composable
fun DynaquizAppNavigation() {
    val navController = rememberNavController()

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Splash,
        ) {
            composable<Splash>(
                exitTransition = { ExitTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                SplashHost(
                    onNavigateToNext = {
                        navController.navigate(Home) {
                            // Remove Splash do back stack não dá pra "voltar" pra splash
                            popUpTo(Splash) { inclusive = true }
                        }
                    },
                    titleModifier = Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = BRAND_TITLE_KEY),
                        animatedVisibilityScope = this@composable,
                        boundsTransform = { _, _ -> tween(durationMillis = BRAND_LOCKUP_DURATION) },
                    ),
                    purpleSurfaceModifier = Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = PURPLE_SURFACE_KEY),
                        animatedVisibilityScope = this@composable,
                        boundsTransform = { _, _ -> tween(durationMillis = BRAND_LOCKUP_DURATION) },
                    ),
                )
            }

            composable<Home>(
                enterTransition = { EnterTransition.None },
                popEnterTransition = { EnterTransition.None },
            ) {
                HomeHost(
                    onNavigateToQuiz = { playerId, challengeMode ->
                        //TODO: implementar
                    },
                    onNavigateToRanking = {
                        //TODO: implementar
                    },
                    titleModifier = Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = BRAND_TITLE_KEY),
                        animatedVisibilityScope = this@composable,
                        boundsTransform = { _, _ -> tween(durationMillis = BRAND_LOCKUP_DURATION) },
                    ),
                    purpleSurfaceModifier = Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = PURPLE_SURFACE_KEY),
                        animatedVisibilityScope = this@composable,
                        boundsTransform = { _, _ -> tween(durationMillis = BRAND_LOCKUP_DURATION) },
                    ),
                )
            }
        }
    }
}

private const val BRAND_TITLE_KEY = "brand_title"
private const val PURPLE_SURFACE_KEY = "purple_surface"
private const val BRAND_LOCKUP_DURATION = 600