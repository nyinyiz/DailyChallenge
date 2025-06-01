package com.nyinyi.dailychallenge.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.nyinyi.dailychallenge.ui.AppState
import com.nyinyi.dailychallenge.ui.AppViewModel
import com.nyinyi.dailychallenge.ui.screens.detail.QuestionDetail
import com.nyinyi.dailychallenge.ui.screens.list.QuestionsList
import com.nyinyi.dailychallenge.ui.screens.play.components.GameMode
import com.nyinyi.dailychallenge.ui.screens.play.mcq.MultipleChoiceScreen
import com.nyinyi.dailychallenge.ui.screens.play.quiz.QuizScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: AppViewModel,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Routes.QuestionList,
    ) {
        composable<Routes.QuestionList> {
            QuestionsList(
                navigateToGameMode = { gameMode ->
                    when (gameMode) {
                        GameMode.TrueOrFalse -> {
                            navController.navigate(Routes.QuizScreen)
                        }

                        GameMode.MultipleChoice -> {
                            navController.navigate(Routes.MultipleChoiceScreen)
                        }

                        GameMode.MultipleSelect -> {
                            // TODO: Handle multiple select
                        }

                        GameMode.MatchingGame -> {
                            // TODO: Handle matching game
                        }
                    }
                },
                onClickChallenge = { question ->
                    navController.navigate(
                        Routes.QuestionDetail(questionId = question.id),
                    )
                },
                onToggleTheme = onToggleTheme,
            )
        }
        composable<Routes.QuestionDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.QuestionDetail>()
            LaunchedEffect(args.questionId) {
                viewModel.getDailyChallengeById(args.questionId)
            }
            when (val state = uiState) {
                is AppState.ContentById -> {
                    QuestionDetail(
                        onBack = { navController.popBackStack() },
                        question = state.dailyChallenge,
                        onToggleTheme = onToggleTheme,
                    )
                }

                AppState.Error -> {
                    // Show error state
                }

                else -> {
                }
            }
        }

        composable<Routes.QuizScreen> {
            QuizScreen(
                onToggleTheme = onToggleTheme,
                onBack = { navController.popBackStack() },
            )
        }

        composable<Routes.MultipleChoiceScreen> {
            MultipleChoiceScreen(
                onBack = { navController.popBackStack() },
                onBackToHome = { navController.popBackStack() },
                onToggleTheme = onToggleTheme,
            )
        }
    }
}
