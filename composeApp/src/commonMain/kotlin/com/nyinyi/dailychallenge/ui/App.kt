package com.nyinyi.dailychallenge.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nyinyi.dailychallenge.di.KoinInitializer
import com.nyinyi.dailychallenge.ui.screens.detail.QuestionDetail
import com.nyinyi.dailychallenge.ui.screens.list.QuestionsList
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeTheme
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private val kotlinInitializer = KoinInitializer.init()

@Composable
@Preview
fun App(viewModel: AppViewModel = koinViewModel()) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    var darkTheme by rememberSaveable { mutableStateOf(true) }

    val navController = rememberNavController()
    DailyChallengeTheme(darkTheme = darkTheme) {
        NavHost(
            navController = navController,
            startDestination = Routes.QuestionList,
        ) {
            composable<Routes.QuestionList> {
                QuestionsList(
                    onClickChallenge = { question ->
                        navController.navigate(
                            Routes.QuestionDetail(questionId = question.id),
                        )
                    },
                    onToggleTheme = { darkTheme = !darkTheme },
                )
            }
            composable<Routes.QuestionDetail> { backStackEntry ->
                val args = backStackEntry.toRoute<Routes.QuestionDetail>()
                LaunchedEffect(args.questionId) {
                    viewModel.getDailyChallengeById(args.questionId)
                }
                when (uiState.value) {
                    is AppState.ContentById -> {
                        QuestionDetail(
                            onBack = { navController.popBackStack() },
                            question = (uiState.value as AppState.ContentById).dailyChallenge,
                            onToggleTheme = { darkTheme = !darkTheme },
                        )
                    }

                    AppState.Error -> {
                        // Show error state
                    }

                    else -> {
                    }
                }
            }
        }
    }
}

sealed interface Routes {
    @Serializable
    data class QuestionDetail(
        val questionId: String,
    ) : Routes

    @Serializable
    data object QuestionList : Routes
}
