package com.nyinyi.dailychallenge.ui.screens.play.mcq

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.ui.components.AppScreenScaffold
import com.nyinyi.dailychallenge.ui.screens.play.components.QuizAnswerOptionCard
import com.nyinyi.dailychallenge.ui.screens.play.mcq.components.MultipleChoiceResultScreen
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.EmptyState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.ErrorState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.LoadingState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.QuestionProgressUI
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeSpacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MultipleChoiceScreen(
    onBack: () -> Unit,
    onBackToHome: () -> Unit,
    onToggleTheme: () -> Unit,
    viewModel: MultipleChoiceViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScreenScaffold(
        onBack = onBack,
        title = "Multiple Choice",
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            when (val state = uiState) {
                is MultipleChoiceUiState.Loading -> LoadingState()
                is MultipleChoiceUiState.Error ->
                    ErrorState(
                        message = state.message,
                        onRetry = viewModel::loadQuestions,
                    )

                is MultipleChoiceUiState.Empty ->
                    EmptyState(
                        onRetry = viewModel::loadQuestions,
                    )

                is MultipleChoiceUiState.Quiz -> {
                    QuestionProgressUI(
                        difficulty = state.difficultyStatus,
                        currentQuestion = state.currentQuestionIndex + 1,
                        totalQuestions = state.totalQuestions,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(DailyChallengeSpacing.large),
                    )

                    if (state.isComplete) {
                        state.result?.let { result ->
                            MultipleChoiceResultScreen(
                                result = result,
                                onRestartQuiz = viewModel::restartQuiz,
                                onBackToHome = onBackToHome,
                            )
                        }
                    } else {
                        QuizContent(
                            quizState = state,
                            onAnswerSelected = viewModel::answerQuestion,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizContent(
    quizState: MultipleChoiceUiState.Quiz,
    onAnswerSelected: (String) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Column {
        quizState.currentQuestion?.let { question ->
            Text(
                text = question.question,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(DailyChallengeSpacing.large),
            )

            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(DailyChallengeSpacing.large),
                verticalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.small),
            ) {
                items(
                    items = question.options,
                    key = { it },
                ) { option ->
                    OptionItem(
                        option = option,
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            onAnswerSelected(option)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun OptionItem(
    option: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    QuizAnswerOptionCard(
        text = option,
        leadingIcon = Icons.Rounded.RadioButtonUnchecked,
        onClick = onClick,
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )
}
