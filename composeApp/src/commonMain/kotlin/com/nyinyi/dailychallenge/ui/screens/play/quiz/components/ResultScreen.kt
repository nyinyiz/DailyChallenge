package com.nyinyi.dailychallenge.ui.screens.play.quiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.QuizResult
import kotlin.math.roundToInt

@Composable
fun ResultScreen(
    result: QuizResult,
    onRetryQuiz: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Quiz Complete!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        Text(
            text = "Score: ${result.correctAnswers}/${result.totalQuestions}",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
        )

        val percentage = (result.correctAnswers.toFloat() / result.totalQuestions.toFloat()) * 100
        Text(
            text = "Percentage: ${(percentage * 10).roundToInt() / 10}%",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        if (result.incorrectAnswers.isNotEmpty()) {
            Text(
                text = "Questions to Review:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            )

            LazyColumn(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
            ) {
                items(
                    items = result.incorrectAnswers,
                    key = { question -> question.id },
                ) { question ->
                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                            ),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = question.question,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = "Correct Answer: ${if (question.correctAnswer) "True" else "False"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                            )
                            Text(
                                text = question.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = onRetryQuiz,
            modifier =
                Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
        ) {
            Text("Try Again")
        }
    }
}
