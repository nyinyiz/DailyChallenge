package com.nyinyi.dailychallenge.feature.play.multiselect.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectResult
import com.nyinyi.dailychallenge.feature.play.shared.components.QuizExpandableReviewCard
import com.nyinyi.dailychallenge.feature.play.shared.components.QuizResultActionsRow

@Composable
fun MultipleSelectResultScreen(
    result: MultipleSelectResult,
    onRestartQuiz: () -> Unit,
    onBackToHome: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Quiz Completed!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your Score",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )

                Text(
                    text = "${result.score}/${result.totalQuestions}",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Percentage: ${(result.score.toFloat() / result.totalQuestions.toFloat() * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (result.incorrectAnswers.isNotEmpty()) {
            Text(
                text = "Questions to Review",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(result.incorrectAnswers) { question ->
                    ReviewMultipleSelectCard(question = question)
                }
            }
        } else {
            Text(
                text = "Perfect Score! You answered all questions correctly.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f).padding(vertical = 32.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        QuizResultActionsRow(
            primaryLabel = "Try Again",
            onPrimaryClick = onRestartQuiz,
            primaryIcon = Icons.Rounded.Refresh,
            secondaryLabel = "Back to Home",
            onSecondaryClick = onBackToHome,
            secondaryIcon = Icons.Rounded.Home,
        )
    }
}

@Composable
private fun ReviewMultipleSelectCard(question: MultipleSelectObj) {
    QuizExpandableReviewCard(
        questionText = question.question,
        answerLabel = "Correct answers: ${question.correctAnswers.joinToString(", ")}",
        explanation = question.explanation.takeIf { it.isNotEmpty() },
    )
}
