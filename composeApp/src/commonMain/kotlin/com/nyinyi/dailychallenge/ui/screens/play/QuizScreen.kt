package com.nyinyi.dailychallenge.ui.screens.play

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.model.QuizResult
import com.nyinyi.dailychallenge.ui.screens.play.components.EmptyState
import com.nyinyi.dailychallenge.ui.screens.play.components.ErrorState
import com.nyinyi.dailychallenge.ui.screens.play.components.LoadingState
import com.nyinyi.dailychallenge.ui.screens.play.components.QuestionProgressUI
import com.nyinyi.dailychallenge.ui.screens.play.components.ResultScreen
import com.nyinyi.dailychallenge.ui.screens.play.components.SwipeInstructions
import com.nyinyi.dailychallenge.ui.screens.play.components.TinderStyleCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuizScreen(viewModel: QuizScreenViewModel = koinViewModel()) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var quizResults by remember { mutableStateOf<QuizResult?>(null) }
    var incorrectAnswers by remember { mutableStateOf(listOf<QuizCard>()) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (val state = uiState) {
            is QuizState.Loading -> {
                LoadingState()
            }

            is QuizState.Error -> {
                ErrorState(
                    message = "Error loading questions",
                    onRetry = {
                    },
                )
            }

            is QuizState.Success -> {
                if (state.quizList.isEmpty()) {
                    EmptyState(
                        onRetry = {
                        },
                    )
                } else {
                    // Question Progress UI
                    QuestionProgressUI(
                        currentQuestion = currentQuestionIndex + 1,
                        totalQuestions = state.quizList.size,
                    )

                    if (quizResults != null) {
                        ResultScreen(
                            result = quizResults!!,
                            onRetryQuiz = {
                                currentQuestionIndex = 0
                                incorrectAnswers = listOf()
                                quizResults = null
                            },
                        )
                    } else if (currentQuestionIndex < state.quizList.size) {
                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            // Swipe Instruction Text
                            SwipeInstructions()

                            // Question Card
                            TinderStyleCard(
                                card = state.quizList[currentQuestionIndex],
                                onSwipeLeft = {
                                    if (state.quizList[currentQuestionIndex].correctAnswer) {
                                        incorrectAnswers =
                                            incorrectAnswers + state.quizList[currentQuestionIndex]
                                    }
                                    currentQuestionIndex++
                                    if (currentQuestionIndex >= state.quizList.size) {
                                        quizResults =
                                            QuizResult(
                                                totalQuestions = state.quizList.size,
                                                correctAnswers = state.quizList.size - incorrectAnswers.size,
                                                incorrectAnswers = incorrectAnswers,
                                            )
                                    }
                                },
                                onSwipeRight = {
                                    if (!state.quizList[currentQuestionIndex].correctAnswer) {
                                        incorrectAnswers =
                                            incorrectAnswers + state.quizList[currentQuestionIndex]
                                    }
                                    currentQuestionIndex++
                                    if (currentQuestionIndex >= state.quizList.size) {
                                        quizResults =
                                            QuizResult(
                                                totalQuestions = state.quizList.size,
                                                correctAnswers = state.quizList.size - incorrectAnswers.size,
                                                incorrectAnswers = incorrectAnswers,
                                            )
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
