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
import androidx.navigation.toRoute
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.QuizPerformance
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.QuizSetup
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.feature.difficulty.DifficultyHost
import com.leanite.dynaquiz.feature.home.HomeHost
import com.leanite.dynaquiz.feature.quiz.QuizHost
import com.leanite.dynaquiz.feature.ranking.RankingHost
import com.leanite.dynaquiz.feature.result.ResultHost
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
                    titleModifier =
                        Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = BRAND_TITLE_KEY),
                            animatedVisibilityScope = this@composable,
                            boundsTransform = { _, _ -> tween(durationMillis = BRAND_LOCKUP_DURATION) },
                        ),
                    purpleSurfaceModifier =
                        Modifier.sharedBounds(
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
                    onNavigateToQuiz = { setup ->
                        navController.navigate(
                            Quiz(
                                playerName = setup.playerName,
                                challengeMode = setup.challengeMode.serializedName,
                            ),
                        )
                    },
                    onNavigateToDifficulty = { navController.navigate(Difficulty) },
                    onNavigateToRanking = { playerName ->
                        navController.navigate(Ranking(playerName = playerName))
                    },
                    titleModifier =
                        Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = BRAND_TITLE_KEY),
                            animatedVisibilityScope = this@composable,
                            boundsTransform = { _, _ -> tween(durationMillis = BRAND_LOCKUP_DURATION) },
                        ),
                    purpleSurfaceModifier =
                        Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = PURPLE_SURFACE_KEY),
                            animatedVisibilityScope = this@composable,
                            boundsTransform = { _, _ -> tween(durationMillis = BRAND_LOCKUP_DURATION) },
                        ),
                )
            }

            composable<Difficulty> {
                DifficultyHost(
                    onNavigateBack = { navController.popBackStack() },
                )
            }

            composable<Quiz> { entry ->
                val args = entry.toRoute<Quiz>()
                QuizHost(
                    setup =
                        QuizSetup(
                            playerName = args.playerName,
                            challengeMode = ChallengeMode.fromSerializedName(args.challengeMode),
                        ),
                    onNavigateToResult = { result ->
                        navController.navigate(
                            Result(
                                playerName = result.setup.playerName,
                                challengeMode = result.setup.challengeMode.serializedName,
                                scorePoints = result.performance.score.points,
                                correctAnswers = result.performance.correctAnswers,
                                totalQuestions = result.performance.totalQuestions,
                            ),
                        ) {
                            // Remove o Quiz do back stack para não voltar pro quiz já terminado
                            popUpTo<Quiz> { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }

            composable<Result> { entry ->
                val args = entry.toRoute<Result>()
                val sessionResult =
                    QuizSessionResult(
                        setup =
                            QuizSetup(
                                playerName = args.playerName,
                                challengeMode = ChallengeMode.fromSerializedName(args.challengeMode),
                            ),
                        performance =
                            QuizPerformance(
                                score = Score(args.scorePoints),
                                correctAnswers = args.correctAnswers,
                                totalQuestions = args.totalQuestions,
                            ),
                    )
                ResultHost(
                    sessionResult = sessionResult,
                    onNavigateToHome = {
                        navController.navigate(Home) {
                            popUpTo(Home) { inclusive = false }
                        }
                    },
                    onNavigateToRanking = { playerName ->
                        navController.navigate(Ranking(playerName = playerName)) {
                            // Ranking substitui Result no stack
                            popUpTo<Result> { inclusive = true }
                        }
                    },
                )
            }

            composable<Ranking> { entry ->
                val args = entry.toRoute<Ranking>()
                RankingHost(
                    playerName = args.playerName,
                    onNavigateBack = { navController.popBackStack() },
                )
            }
        }
    }
}

private const val BRAND_TITLE_KEY = "brand_title"
private const val PURPLE_SURFACE_KEY = "purple_surface"
private const val BRAND_LOCKUP_DURATION = 600
