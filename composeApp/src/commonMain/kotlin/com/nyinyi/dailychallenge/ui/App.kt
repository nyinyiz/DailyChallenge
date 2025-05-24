package com.nyinyi.dailychallenge.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.model.getDailyChallengeList
import com.nyinyi.dailychallenge.ui.screens.detail.QuestionDetail
import com.nyinyi.dailychallenge.ui.screens.list.QuestionsList
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeTheme
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
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
                var challengeState by remember { mutableStateOf<DailyChallengeObj?>(null) }

                LaunchedEffect(args.questionId) {
                    val fullList = getDailyChallengeList()
                    challengeState = fullList.firstOrNull { it.id == args.questionId }
                }
                challengeState?.let { loadedChallenge ->
                    QuestionDetail(
                        onBack = { navController.popBackStack() },
                        question = loadedChallenge,
                        onToggleTheme = { darkTheme = !darkTheme },
                    )
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
