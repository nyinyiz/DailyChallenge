package com.nyinyi.dailychallenge.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.nyinyi.dailychallenge.feature.challenge.detail.QuestionDetail
import com.nyinyi.dailychallenge.feature.challenge.detail.QuestionDetailUiState
import com.nyinyi.dailychallenge.feature.challenge.detail.QuestionDetailViewModel
import com.nyinyi.dailychallenge.feature.challenge.list.AppTab
import com.nyinyi.dailychallenge.feature.challenge.list.ChallengeListTabScreen
import com.nyinyi.dailychallenge.feature.challenge.list.HomeTabScreen
import com.nyinyi.dailychallenge.feature.challenge.list.ProfileTabScreen
import com.nyinyi.dailychallenge.feature.play.home.components.PlayMode
import com.nyinyi.dailychallenge.feature.play.matching.MatchingGameScreen
import com.nyinyi.dailychallenge.feature.play.mcq.MultipleChoiceScreen
import com.nyinyi.dailychallenge.feature.play.multiselect.MultipleSelectScreen
import com.nyinyi.dailychallenge.feature.play.truefalse.TrueFalseScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    onToggleTheme: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home,
    ) {
        composable<Routes.Home> {
            HomeTabScreen(
                onTabSelected = { tab ->
                    navController.navigateToTab(tab)
                },
                navigateToPlayMode = { playMode ->
                    when (playMode) {
                        PlayMode.TrueOrFalse -> {
                            navController.navigate(Routes.TrueFalseScreen)
                        }

                        PlayMode.MultipleChoice -> {
                            navController.navigate(Routes.MultipleChoiceScreen)
                        }

                        PlayMode.MultipleSelect -> {
                            navController.navigate(Routes.MultipleSelectScreen)
                        }

                        PlayMode.MatchingGame -> {
                            navController.navigate(Routes.MatchingGameScreen)
                        }
                    }
                },
                onClickChallenge = { question ->
                    navController.navigate(
                        Routes.QuestionDetail(questionId = question.id),
                    )
                },
            )
        }
        composable<Routes.ChallengeList> {
            ChallengeListTabScreen(
                onTabSelected = { tab ->
                    navController.navigateToTab(tab)
                },
                onClickChallenge = { question ->
                    navController.navigate(Routes.QuestionDetail(questionId = question.id))
                },
            )
        }
        composable<Routes.Profile> {
            ProfileTabScreen(
                onTabSelected = { tab ->
                    navController.navigateToTab(tab)
                },
            )
        }
        composable<Routes.QuestionDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.QuestionDetail>()
            val viewModel: QuestionDetailViewModel = koinViewModel()
            val uiState by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(args.questionId) {
                viewModel.loadQuestion(args.questionId)
            }

            when (val state = uiState) {
                is QuestionDetailUiState.Success -> {
                    QuestionDetail(
                        onBack = {
                            navController.popBackStack()
                        },
                        question = state.question,
                        onToggleTheme = onToggleTheme,
                    )
                }

                is QuestionDetailUiState.Error -> {
                    Text(text = state.message)
                }

                QuestionDetailUiState.Loading -> Text(text = "Loading...")
            }
        }

        composable<Routes.TrueFalseScreen> {
            TrueFalseScreen(
                onToggleTheme = onToggleTheme,
                onBack = { navController.popBackStack() },
            )
        }

        composable<Routes.MultipleChoiceScreen> {
            MultipleChoiceScreen(
                onBack = { navController.popBackStack() },
                onBackToHome = {
                    navController.navigateToTab(AppTab.Home)
                },
                onToggleTheme = onToggleTheme,
            )
        }

        composable<Routes.MultipleSelectScreen> {
            MultipleSelectScreen(
                onBack = { navController.popBackStack() },
                onBackToHome = {
                    navController.navigateToTab(AppTab.Home)
                },
                onToggleTheme = onToggleTheme,
            )
        }

        composable<Routes.MatchingGameScreen> {
            MatchingGameScreen(
                onBack = { navController.popBackStack() },
                onBackToHome = {
                    navController.navigateToTab(AppTab.Home)
                },
                onToggleTheme = onToggleTheme,
            )
        }
    }
}

private fun NavHostController.navigateToTab(tab: AppTab) {
    val route =
        when (tab) {
            AppTab.Home -> Routes.Home
            AppTab.List -> Routes.ChallengeList
            AppTab.Profile -> Routes.Profile
        }

    navigate(route) {
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
