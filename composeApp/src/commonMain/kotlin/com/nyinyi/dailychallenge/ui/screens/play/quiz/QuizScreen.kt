package com.nyinyi.dailychallenge.ui.screens.play.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.core.ui.components.AppScreenScaffold
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.EmptyState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.ErrorState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.LoadingState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.QuestionProgressUI
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.ResultScreen
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.SwipeInstructions
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.TinderStyleCard
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeSpacing
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun QuizScreenPreview() {
    QuizScreen(
        onBack = {},
        onToggleTheme = {},
    )
}

@Composable
fun QuizScreen(
    onBack: () -> Unit,
    onToggleTheme: () -> Unit = {},
    viewModel: QuizScreenViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    var swipeOffsetX by remember { mutableStateOf(0f) }
    val hapticFeedback = LocalHapticFeedback.current

    AppScreenScaffold(
        onBack = onBack,
        title = "True or False",
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val state = uiState) {
                is QuizState.Loading -> {
                    LoadingState()
                }

                is QuizState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = viewModel::getTrueFalseChallenges,
                    )
                }

                is QuizState.Empty ->
                    EmptyState(
                        onRetry = viewModel::getTrueFalseChallenges,
                    )

                is QuizState.Success -> {
                    val session = state.session

                    QuestionProgressUI(
                        difficulty = session.difficultyStatus,
                        currentQuestion = session.currentQuestionIndex + 1,
                        totalQuestions = session.totalQuestions,
                    )

                    session.result?.let { result ->
                        ResultScreen(
                            result = result,
                            onRetryQuiz = {
                                swipeOffsetX = 0f
                                viewModel.restartQuiz()
                            },
                        )
                    } ?: session.currentQuestion?.let { currentQuestion ->
                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            SwipeInstructions()

                            if (swipeOffsetX < 0) {
                                Box(
                                    modifier =
                                            Modifier
                                                .align(Alignment.CenterStart)
                                                .fillMaxHeight()
                                                .width((-swipeOffsetX / 5).dp.coerceIn(0.dp, DailyChallengeSpacing.xLarge))
                                                .background(
                                                    color = MaterialTheme.colorScheme.error,
                                                    shape =
                                                        RoundedCornerShape(
                                                            topEnd = DailyChallengeSpacing.large,
                                                            bottomEnd = DailyChallengeSpacing.large,
                                                        ),
                                                ),
                                )
                            }

                            if (swipeOffsetX > 0) {
                                Box(
                                    modifier =
                                            Modifier
                                                .align(Alignment.CenterEnd)
                                                .fillMaxHeight()
                                                .width((swipeOffsetX / 5).dp.coerceIn(0.dp, DailyChallengeSpacing.xLarge))
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape =
                                                        RoundedCornerShape(
                                                            topStart = DailyChallengeSpacing.large,
                                                            bottomStart = DailyChallengeSpacing.large,
                                                        ),
                                                ),
                                )
                            }

                            TinderStyleCard(
                                card = currentQuestion,
                                onSwipeLeft = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.answerQuestion(false)
                                },
                                onSwipeRight = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.answerQuestion(true)
                                },
                                onDrag = { offsetX ->
                                    swipeOffsetX = offsetX
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
