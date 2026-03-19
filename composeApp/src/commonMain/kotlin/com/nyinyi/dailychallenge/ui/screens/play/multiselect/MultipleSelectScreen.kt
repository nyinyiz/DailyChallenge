package com.nyinyi.dailychallenge.ui.screens.play.multiselect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.ui.components.AppScreenScaffold
import com.nyinyi.dailychallenge.ui.screens.play.components.QuizAnswerOptionCard
import com.nyinyi.dailychallenge.ui.screens.play.multiselect.components.MultipleSelectResultScreen
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.EmptyState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.ErrorState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.LoadingState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.QuestionProgressUI
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeSpacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MultipleSelectScreen(
    onBack: () -> Unit,
    onBackToHome: () -> Unit,
    onToggleTheme: () -> Unit,
    viewModel: MultipleSelectViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScreenScaffold(
        onBack = onBack,
        title = "Multiple Select",
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            when (val state = uiState) {
                is MultipleSelectUiState.Loading -> LoadingState()
                is MultipleSelectUiState.Error ->
                    ErrorState(
                        message = state.message,
                        onRetry = viewModel::loadQuestions,
                    )

                is MultipleSelectUiState.Empty ->
                    EmptyState(
                        onRetry = viewModel::loadQuestions,
                    )

                is MultipleSelectUiState.Quiz -> {
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
                            MultipleSelectResultScreen(
                                result = result,
                                onRestartQuiz = viewModel::restartQuiz,
                                onBackToHome = onBackToHome,
                            )
                        }
                    } else {
                        QuizContent(
                            quizState = state,
                            onOptionToggled = viewModel::toggleOptionSelection,
                            onSubmit = viewModel::submitAnswer,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizContent(
    quizState: MultipleSelectUiState.Quiz,
    onOptionToggled: (String) -> Unit,
    onSubmit: () -> Unit,
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
                        .fillMaxWidth()
                        .padding(DailyChallengeSpacing.large)
                        .weight(1f),
                verticalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.small),
            ) {
                items(
                    items = question.options,
                    key = { it },
                ) { option ->
                    OptionItem(
                        option = option,
                        isSelected = quizState.selectedOptions.contains(option),
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            onOptionToggled(option)
                        },
                    )
                }
            }

            // Submit button
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(DailyChallengeSpacing.large),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSubmit()
                    },
                    enabled = quizState.selectedOptions.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Submit",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(DailyChallengeSpacing.large))
        }
    }
}

@Composable
fun OptionItem(
    option: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    QuizAnswerOptionCard(
        text = option,
        leadingIcon = if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
        onClick = onClick,
        modifier = modifier,
        containerColor =
            if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        contentColor =
            if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSecondaryContainer
            },
        iconTint =
            if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSecondaryContainer
            },
    )
}
