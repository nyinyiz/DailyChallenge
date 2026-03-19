package com.nyinyi.dailychallenge.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.FailedQuestionRecord
import com.nyinyi.dailychallenge.data.model.UserProfile

@Composable
fun FailedExercisesSection(
    userProfile: UserProfile,
    onClearFailedExercises: () -> Unit,
) {
    val hasFailedExercises =
        userProfile.failedQuizCards.isNotEmpty() ||
            userProfile.failedMultipleChoiceQuestions.isNotEmpty() ||
            userProfile.failedMultipleSelectQuestions.isNotEmpty() ||
            userProfile.failedMatchingGameQuestions.isNotEmpty()

    if (hasFailedExercises) {
        FailedExercisesHeader(onClearFailedExercises)
        FailedExercisesList(userProfile)
    } else {
        NoFailedExercisesCard()
    }
}

@Composable
private fun FailedExercisesHeader(onClearFailedExercises: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Failed Exercises",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        TextButton(
            onClick = onClearFailedExercises,
        ) {
            Text(
                "Clear All",
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun NoFailedExercisesCard() {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                text = "No Failed Exercises",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Great job! You haven't failed any exercises yet. Keep up the good work!",
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun FailedExercisesList(userProfile: UserProfile) {
    if (userProfile.failedQuizCards.isNotEmpty()) {
        FailedExerciseSection(
            title = "True/False Questions",
            items = userProfile.failedQuizCards,
        )
    }

    if (userProfile.failedMultipleChoiceQuestions.isNotEmpty()) {
        FailedExerciseSection(
            title = "Multiple Choice Questions",
            items = userProfile.failedMultipleChoiceQuestions,
        )
    }

    if (userProfile.failedMultipleSelectQuestions.isNotEmpty()) {
        FailedExerciseSection(
            title = "Multiple Select Questions",
            items = userProfile.failedMultipleSelectQuestions,
        )
    }

    if (userProfile.failedMatchingGameQuestions.isNotEmpty()) {
        FailedExerciseSection(
            title = "Matching Game Questions",
            items = userProfile.failedMatchingGameQuestions,
        )
    }
}

@Composable
private fun FailedExerciseSection(
    title: String,
    items: List<FailedQuestionRecord>,
) {
    // State to toggle expand/collapse
    var isExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
        ) {
            // Indicator Strip
            Box(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .width(6.dp)
                        .background(MaterialTheme.colorScheme.error),
            )

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(16.dp),
            ) {
                // Title Row with Toggle Icon
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { isExpanded = isExpanded.not() },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title + " (${items.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                // Conditionally show the list items
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(12.dp))
                    items.forEach { item ->
                        FailedQuestionItem(
                            record = item,
                            sectionTitle = title,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
