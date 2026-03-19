package com.nyinyi.dailychallenge.ui.screens.play.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun CircularQuizResultSummary(
    percentage: Float,
    scoreLabel: String,
    modifier: Modifier = Modifier,
) {
    val scoreColor =
        when {
            percentage >= 80 -> MaterialTheme.colorScheme.primary
            percentage >= 60 -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.error
        }

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + expandVertically(),
        modifier = modifier,
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

            Spacer(modifier = Modifier.size(24.dp))

            Box(
                modifier =
                    Modifier
                        .size(200.dp)
                        .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                var progressAnimation by remember { mutableFloatStateOf(0f) }
                LaunchedEffect(percentage) {
                    animate(
                        initialValue = 0f,
                        targetValue = percentage / 100f,
                        animationSpec = tween(1000, easing = FastOutSlowInEasing),
                    ) { value, _ -> progressAnimation = value }
                }

                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 12.dp,
                )
                CircularProgressIndicator(
                    progress = { progressAnimation },
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
                        text = scoreLabel,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun QuizExpandableReviewCard(
    questionText: String,
    answerLabel: String,
    modifier: Modifier = Modifier,
    explanation: String? = null,
    indicatorColor: Color = MaterialTheme.colorScheme.error,
) {
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier =
            modifier
                .fillMaxWidth()
                .animateContentSize()
                .clickable { expanded = !expanded },
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .width(6.dp)
                        .background(indicatorColor),
            )

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = questionText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        imageVector =
                            if (expanded) {
                                Icons.Rounded.ExpandLess
                            } else {
                                Icons.Rounded.ExpandMore
                            },
                        contentDescription = if (expanded) "Show less" else "Show more",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier.padding(top = 12.dp),
                    ) {
                        Text(
                            text = answerLabel,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )

                        if (!explanation.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = explanation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizResultActionsRow(
    primaryLabel: String,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryIcon: ImageVector? = null,
    secondaryLabel: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    secondaryIcon: ImageVector? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (secondaryLabel != null && onSecondaryClick != null) {
            OutlinedButton(
                onClick = onSecondaryClick,
                modifier = Modifier.weight(1f),
            ) {
                if (secondaryIcon != null) {
                    Icon(
                        imageVector = secondaryIcon,
                        contentDescription = null,
                    )
                }
                Text(
                    text = secondaryLabel,
                    modifier = Modifier.padding(start = if (secondaryIcon != null) 8.dp else 0.dp),
                )
            }
        }

        Button(
            onClick = onPrimaryClick,
            modifier = Modifier.weight(1f),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
        ) {
            if (primaryIcon != null) {
                Icon(
                    imageVector = primaryIcon,
                    contentDescription = null,
                )
            }
            Text(
                text = primaryLabel,
                modifier = Modifier.padding(start = if (primaryIcon != null) 8.dp else 0.dp),
            )
        }
    }
}
