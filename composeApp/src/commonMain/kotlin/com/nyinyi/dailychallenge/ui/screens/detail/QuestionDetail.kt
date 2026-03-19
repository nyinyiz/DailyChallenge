package com.nyinyi.dailychallenge.ui.screens.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.ui.components.AppScreenScaffold
import com.nyinyi.dailychallenge.ui.components.QuestionTab
import com.nyinyi.dailychallenge.ui.components.SolutionTab
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeShapes
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeSpacing

@Composable
fun QuestionDetail(
    onBack: () -> Unit,
    question: DailyChallengeObj,
    onToggleTheme: () -> Unit = {},
) {
    var showContent by remember { mutableStateOf(false) }
    var isFlipped by remember { mutableStateOf(false) }

    val difficulty = question.difficulty

    val difficultyColor =
        when (difficulty.lowercase()) {
            "easy" -> MaterialTheme.colorScheme.tertiary
            "medium" -> MaterialTheme.colorScheme.secondary
            "hard" -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.error // fallback color
        }

    val rotation =
        animateFloatAsState(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec = tween(500, easing = FastOutSlowInEasing),
            label = "cardFlip",
        )

    LaunchedEffect(Unit) {
        showContent = true
    }

    AppScreenScaffold(
        onBack = onBack,
        titleContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Challenge ${question.id}",
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                )
                Spacer(modifier = Modifier.width(DailyChallengeSpacing.small))
                Box(
                    modifier =
                        Modifier
                            .clip(DailyChallengeShapes.medium)
                            .background(difficultyColor.copy(alpha = 0.2f))
                            .padding(horizontal = DailyChallengeSpacing.small, vertical = 2.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = difficulty,
                        style = MaterialTheme.typography.labelSmall,
                        color = difficultyColor,
                    )
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DailyChallengeSpacing.large),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    onClick = { isFlipped = !isFlipped },
                    modifier =
                        Modifier
                            .padding(horizontal = DailyChallengeSpacing.large),
                    colors =
                        androidx.compose.material3.ButtonDefaults.textButtonColors(
                            contentColor = if (isFlipped) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        ),
                ) {
                    Text(
                        text = if (isFlipped) "✨ Show Question" else "\uD83D\uDCA1 Show Solution",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 }),
            ) {
                Box(
                    modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = DailyChallengeSpacing.large),
                ) {
                    Card(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    rotationY = rotation.value
                                    cameraDistance = 12f * density
                                    alpha = if (rotation.value > 90f) 0f else 1f
                                },
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ),
                        elevation = CardDefaults.cardElevation(0.dp),
                    ) {
                        QuestionTab(question)
                    }

                    Card(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    rotationY = rotation.value - 180f
                                    cameraDistance = 12f * density
                                    alpha = if (rotation.value < 90f) 0f else 1f
                                },
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ),
                        elevation = CardDefaults.cardElevation(0.dp),
                    ) {
                        SolutionTab(question)
                    }
                }
            }
        }
    }
}
