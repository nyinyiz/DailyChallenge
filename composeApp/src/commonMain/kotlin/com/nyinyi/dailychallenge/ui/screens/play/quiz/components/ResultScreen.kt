package com.nyinyi.dailychallenge.ui.screens.play.quiz.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.model.QuizResult
import kotlin.math.roundToInt

@Composable
fun ResultScreen(
    result: QuizResult,
    onRetryQuiz: () -> Unit,
) {
    val percentage = (result.correctAnswers.toFloat() / result.totalQuestions.toFloat()) * 100
    val scoreColor =
        when {
            percentage >= 80 -> MaterialTheme.colorScheme.primary
            percentage >= 60 -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.error
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Result Header
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text =
                            when {
                                percentage >= 80 -> "Excellent!"
                                percentage >= 60 -> "Good Job!"
                                else -> "Keep Practicing!"
                            },
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Score Circle
                    Box(
                        modifier =
                            Modifier
                                .size(200.dp)
                                .padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        var progressAnimation by remember { mutableFloatStateOf(0f) }
                        LaunchedEffect(Unit) {
                            animate(
                                initialValue = 0f,
                                targetValue = percentage / 100f,
                                animationSpec = tween(1000, easing = FastOutSlowInEasing),
                            ) { value, _ -> progressAnimation = value }
                        }

                        CircularProgressIndicator(
                            progress = 1f,
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            strokeWidth = 12.dp,
                        )
                        CircularProgressIndicator(
                            progress = progressAnimation,
                            modifier = Modifier.fillMaxSize(),
                            color = scoreColor,
                            strokeWidth = 12.dp,
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "${(percentage * 10).roundToInt() / 10}%",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = scoreColor,
                            )
                            Text(
                                text = "${result.correctAnswers}/${result.totalQuestions}",
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Review Section
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
                    items(
                        items = result.incorrectAnswers,
                    ) { question ->
                        ReviewQuestionCard(question = question)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(
                    onClick = onRetryQuiz,
                    modifier = Modifier.weight(1f),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    val textLabel = if (result.incorrectAnswers.isEmpty()) "Next" else "Retry"
                    Text(textLabel)
                }
            }
        }
    }
}

@Composable
private fun ReviewQuestionCard(question: QuizCard) {
    ElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .animateContentSize(),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
            ),
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector =
                            if (expanded) {
                                Icons.Rounded.ExpandLess
                            } else {
                                Icons.Rounded.ExpandMore
                            },
                        contentDescription = if (expanded) "Show less" else "Show more",
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                ) {
                    Text(
                        text = "Correct Answer: ${if (question.correctAnswer) "True" else "False"}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                    )
                }
            }
        }
    }
}
