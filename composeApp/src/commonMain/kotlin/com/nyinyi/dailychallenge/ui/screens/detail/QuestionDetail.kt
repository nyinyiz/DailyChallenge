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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
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
import com.nyinyi.dailychallenge.ui.components.QuestionTab
import com.nyinyi.dailychallenge.ui.components.SolutionTab
import com.nyinyi.dailychallenge.ui.theme.ThemeColors

@OptIn(ExperimentalMaterial3Api::class)
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
        when (difficulty) {
            "Easy" -> MaterialTheme.colorScheme.tertiary
            "Medium" -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.error
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
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
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(difficultyColor.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
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
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Outlined.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
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
            )
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
                        .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    onClick = { isFlipped = !isFlipped },
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp),
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
                            .padding(horizontal = 16.dp),
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
                        elevation = CardDefaults.cardElevation(4.dp),
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
                        elevation = CardDefaults.cardElevation(4.dp),
                    ) {
                        SolutionTab(question)
                    }
                }
            }
        }
    }
}
