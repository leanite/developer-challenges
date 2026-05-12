package com.leanite.dynaquiz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leanite.dynaquiz.feature.home.HomeHost
import com.leanite.dynaquiz.feature.splash.SplashHost

@Composable
fun DynaquizAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Splash,
    ) {
        composable<Splash> {
            SplashHost(
                onNavigateToNext = {
                    navController.navigate(Home) {
                        // Remove Splash do back stack não dá pra "voltar" pra splash
                        popUpTo(Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            HomeHost(
                onNavigateToQuiz = { playerId, challengeMode ->
                    //TODO: implementar
                },
                onNavigateToRanking = {
                    //TODO: implementar
                },
            )
        }
    }
}