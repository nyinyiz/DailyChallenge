package com.nyinyi.dailychallenge.feature.play.mcq.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceResult
import com.nyinyi.dailychallenge.feature.play.shared.components.CircularQuizResultSummary
import com.nyinyi.dailychallenge.feature.play.shared.components.QuizExpandableReviewCard
import com.nyinyi.dailychallenge.feature.play.shared.components.QuizResultActionsRow

@Composable
fun MultipleChoiceResultScreen(
    result: MultipleChoiceResult,
    onRestartQuiz: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val percentage = (result.score.toFloat() / result.totalQuestions.toFloat()) * 100

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularQuizResultSummary(
                percentage = percentage,
                scoreLabel = "${result.score}/${result.totalQuestions}",
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (result.incorrectAnswers.isNotEmpty()) {
                Text(
                    text = "Questions to Review",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(result.incorrectAnswers) { question ->
                        ReviewMultipleChoiceCard(question = question)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            QuizResultActionsRow(
                primaryLabel = if (result.incorrectAnswers.isEmpty()) "Next" else "Retry",
                onPrimaryClick = onRestartQuiz,
                primaryIcon = Icons.Rounded.Refresh,
            )
        }
    }
}

@Composable
private fun ReviewMultipleChoiceCard(question: MultipleChoiceObj) {
    QuizExpandableReviewCard(
        questionText = question.question,
        answerLabel = "Correct Answer: ${question.correctAnswer}",
        explanation = question.explanation,
    )
}
