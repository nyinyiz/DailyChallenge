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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.model.QuizResult
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.EmptyState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.ErrorState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.LoadingState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.QuestionProgressUI
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.ResultScreen
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.SwipeInstructions
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.TinderStyleCard
import com.nyinyi.dailychallenge.ui.theme.ThemeColors
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onBack: () -> Unit,
    onToggleTheme: () -> Unit = {},
    viewModel: QuizScreenViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var quizResults by remember { mutableStateOf<QuizResult?>(null) }
    var incorrectAnswers by remember { mutableStateOf(listOf<QuizCard>()) }
    var swipeOffsetX by remember { mutableStateOf(0f) }
    val hapticFeedback = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "True or False",
                            style =
                                MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                ),
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Outlined.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
            )
        },
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
                        val difficultyStatus =
                            if (currentQuestionIndex < state.quizList.size) {
                                state.quizList[currentQuestionIndex].difficulty
                            } else {
                                "completed"
                            }

                        QuestionProgressUI(
                            difficulty = difficultyStatus,
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
                                    viewModel.getTrueFalseChallenges()
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

                                // Left Indicator (False/Red)
                                if (swipeOffsetX < 0) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .align(Alignment.CenterStart)
                                                .fillMaxHeight()
                                                .width((-swipeOffsetX / 5).dp.coerceIn(0.dp, 24.dp))
                                                .background(
                                                    color = MaterialTheme.colorScheme.error,
                                                    shape =
                                                        RoundedCornerShape(
                                                            topEnd = 16.dp,
                                                            bottomEnd = 16.dp,
                                                        ),
                                                ),
                                    )
                                }

                                // Right Indicator (True/Blue)
                                if (swipeOffsetX > 0) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .align(Alignment.CenterEnd)
                                                .fillMaxHeight()
                                                .width((swipeOffsetX / 5).dp.coerceIn(0.dp, 24.dp))
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape =
                                                        RoundedCornerShape(
                                                            topStart = 16.dp,
                                                            bottomStart = 16.dp,
                                                        ),
                                                ),
                                    )
                                }

                                // Question Card
                                TinderStyleCard(
                                    card = state.quizList[currentQuestionIndex],
                                    onSwipeLeft = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        if (state.quizList[currentQuestionIndex].correctAnswer) {
                                            incorrectAnswers =
                                                incorrectAnswers + state.quizList[currentQuestionIndex]
                                            // Save failed question to user profile
                                            viewModel.saveFailedQuestion(state.quizList[currentQuestionIndex])
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
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        if (!state.quizList[currentQuestionIndex].correctAnswer) {
                                            incorrectAnswers =
                                                incorrectAnswers + state.quizList[currentQuestionIndex]
                                            // Save failed question to user profile
                                            viewModel.saveFailedQuestion(state.quizList[currentQuestionIndex])
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
}
