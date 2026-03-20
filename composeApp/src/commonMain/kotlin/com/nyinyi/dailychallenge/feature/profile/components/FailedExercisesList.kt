package com.nyinyi.dailychallenge.feature.profile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.nyinyi.dailychallenge.feature.profile.reviewGroups
import com.nyinyi.dailychallenge.feature.profile.totalReviewItemCount

@Composable
fun FailedExercisesSection(
    userProfile: UserProfile,
    onClearFailedExercises: () -> Unit,
) {
    val groups = userProfile.reviewGroups()
    val hasFailedExercises = groups.isNotEmpty()

    SectionContainer(
        title = "Review Queue",
        action = {
            if (hasFailedExercises) {
                TextButton(onClick = onClearFailedExercises) {
                    Text("Clear All", color = MaterialTheme.colorScheme.error)
                }
            }
        },
    ) {
        if (!hasFailedExercises) {
            EmptyReviewState()
        } else {
            InfoRow(
                label = "Saved questions",
                value = userProfile.totalReviewItemCount.toString(),
            )

            Spacer(modifier = Modifier.height(4.dp))

            groups.forEach { (title, items) ->
                FailedExerciseGroupCard(
                    title = title,
                    items = items,
                )
            }
        }
    }
}

@Composable
private fun EmptyReviewState() {
    ElevatedCard(
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.AutoGraph,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "No review backlog",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Strong work. Your profile is clear of saved misses, so this is a good time to push into harder challenges.",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun FailedExerciseGroupCard(
    title: String,
    items: List<FailedQuestionRecord>,
) {
    var isExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.34f),
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = isExpanded.not() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "${items.size} ${if (items.size == 1) "question" else "questions"} waiting",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items.forEach { item ->
                        FailedQuestionItem(
                            record = item,
                            sectionTitle = title,
                        )
                    }
                }
            }
        }
    }
}
