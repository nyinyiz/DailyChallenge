package com.nyinyi.dailychallenge.ui.screens.play.multiselect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.ui.screens.play.multiselect.components.MultipleSelectResultScreen
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.EmptyState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.ErrorState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.LoadingState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.QuestionProgressUI
import com.nyinyi.dailychallenge.ui.theme.ThemeColors
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleSelectScreen(
    onBack: () -> Unit,
    onBackToHome: () -> Unit,
    onToggleTheme: () -> Unit,
    viewModel: MultipleSelectViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Multiple Select",
                        style =
                            MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIosNew,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    Card(
                        shape = CircleShape,
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            ),
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        IconButton(onClick = onToggleTheme) {
                            Text(
                                text = if (ThemeColors.isDarkTheme) "☀️" else "🌙",
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
            )
        },
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
                    val difficultStatus =
                        if (state.currentQuestion?.difficulty != null && state.currentQuestion.difficulty.isNotEmpty()) {
                            state.currentQuestion.difficulty.lowercase()
                        } else {
                            "completed"
                        }

                    QuestionProgressUI(
                        difficulty = difficultStatus,
                        currentQuestion = state.currentQuestionIndex + 1,
                        totalQuestions = state.totalQuestions,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    )

                    if (state.isComplete) {
                        viewModel.getQuizResult()?.let { result ->
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
                modifier = Modifier.padding(16.dp),
            )

            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSubmit()
                    },
                    enabled = quizState.selectedOptions.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Submit",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionItem(
    option: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        onClick = {
            onClick()
        },
        modifier =
            modifier
                .fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = if (isSelected) 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.surface,
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (isSelected) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isSelected) 
                        MaterialTheme.colorScheme.onPrimaryContainer 
                    else 
                        MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}
