package com.nyinyi.dailychallenge.feature.play.mcq

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
import com.nyinyi.dailychallenge.core.ui.components.AppScreenScaffold
import com.nyinyi.dailychallenge.feature.play.shared.components.QuizAnswerOptionCard
import com.nyinyi.dailychallenge.feature.play.mcq.components.MultipleChoiceResultScreen
import com.nyinyi.dailychallenge.feature.play.shared.components.EmptyPlayState
import com.nyinyi.dailychallenge.feature.play.shared.components.ErrorPlayState
import com.nyinyi.dailychallenge.feature.play.shared.components.LoadingPlayState
import com.nyinyi.dailychallenge.feature.play.shared.components.QuestionProgressHeader
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
                is MultipleChoiceUiState.Loading -> LoadingPlayState()
                is MultipleChoiceUiState.Error ->
                    ErrorPlayState(
                        message = state.message,
                        onRetry = viewModel::loadQuestions,
                    )

                is MultipleChoiceUiState.Empty ->
                    EmptyPlayState(
                        onRetry = viewModel::loadQuestions,
                    )

                is MultipleChoiceUiState.Quiz -> {
                    val session = state.session
                    QuestionProgressHeader(
                        difficulty = session.difficultyStatus,
                        currentQuestion = session.currentQuestionIndex + 1,
                        totalQuestions = session.totalQuestions,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(DailyChallengeSpacing.large),
                    )

                    if (session.isComplete) {
                        session.result?.let { result ->
                            MultipleChoiceResultScreen(
                                result = result,
                                onRestartQuiz = viewModel::restartQuiz,
                                onBackToHome = onBackToHome,
                            )
                        }
                    } else {
                        QuizContent(
                            session = session,
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
    session: com.nyinyi.dailychallenge.feature.play.mcq.MultipleChoiceSession,
    onAnswerSelected: (String) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Column {
        session.currentQuestion?.let { question ->
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
