package com.nyinyi.dailychallenge.ui.screens.play

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.model.QuizResult
import com.nyinyi.dailychallenge.data.model.loadTrueFalseQuizQuestions
import com.nyinyi.dailychallenge.ui.screens.play.components.EmptyState
import com.nyinyi.dailychallenge.ui.screens.play.components.ErrorState
import com.nyinyi.dailychallenge.ui.screens.play.components.LoadingState
import com.nyinyi.dailychallenge.ui.screens.play.components.QuestionProgressUI
import com.nyinyi.dailychallenge.ui.screens.play.components.ResultScreen
import com.nyinyi.dailychallenge.ui.screens.play.components.SwipeInstructions
import com.nyinyi.dailychallenge.ui.screens.play.components.TinderStyleCard

@Composable
fun QuizScreen() {
    var questions by remember { mutableStateOf<List<QuizCard>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            questions = loadTrueFalseQuizQuestions()
            isLoading = false
        } catch (e: Exception) {
            error = e.message ?: "Failed to load questions"
            isLoading = false
        }
    }

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
        when {
            isLoading -> {
                LoadingState()
            }

            error != null -> {
                ErrorState(
                    message = error!!,
                    onRetry = {
                    },
                )
            }

            questions.isEmpty() -> {
                EmptyState(
                    onRetry = {
                    },
                )
            }

            else -> {
                // Question Progress UI
                QuestionProgressUI(
                    currentQuestion = currentQuestionIndex + 1,
                    totalQuestions = questions.size,
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
                } else if (currentQuestionIndex < questions.size) {
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
                            card = questions[currentQuestionIndex],
                            onSwipeLeft = {
                                if (questions[currentQuestionIndex].correctAnswer) {
                                    incorrectAnswers =
                                        incorrectAnswers + questions[currentQuestionIndex]
                                }
                                currentQuestionIndex++
                                if (currentQuestionIndex >= questions.size) {
                                    quizResults =
                                        QuizResult(
                                            totalQuestions = questions.size,
                                            correctAnswers = questions.size - incorrectAnswers.size,
                                            incorrectAnswers = incorrectAnswers,
                                        )
                                }
                            },
                            onSwipeRight = {
                                if (!questions[currentQuestionIndex].correctAnswer) {
                                    incorrectAnswers =
                                        incorrectAnswers + questions[currentQuestionIndex]
                                }
                                currentQuestionIndex++
                                if (currentQuestionIndex >= questions.size) {
                                    quizResults =
                                        QuizResult(
                                            totalQuestions = questions.size,
                                            correctAnswers = questions.size - incorrectAnswers.size,
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

@Composable
fun PlayScreenContent() {
    QuizScreen()
}
