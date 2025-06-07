package com.nyinyi.dailychallenge.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun DailyChallengeListContentPreview() {
    DailyChallengeListContent(onClickChallenge = {})
}

@Composable
fun DailyChallengeListContent(
    viewModel: QuestionListViewModel = koinViewModel(),
    onClickChallenge: (DailyChallengeObj) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (val state = uiState) {
            is QuestionListState.Loading -> {
                LoadingAnimation()
            }

            is QuestionListState.Success -> {
                LazyColumn(
                    state = listState,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                ) {
                    itemsIndexed(state.dailyChallenges) { index, challenge ->
                        ChallengeCard(
                            challenge = challenge,
                            index = index,
                            onClickChallenge = {
                                println("Clicked challenge: ${challenge.id}")
                                onClickChallenge(challenge)
                            },
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            is QuestionListState.Error -> {
                ErrorSection()
            }
        }
    }
}

@Composable
private fun ChallengeCard(
    challenge: DailyChallengeObj,
    index: Int,
    onClickChallenge: (DailyChallengeObj) -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }

    val difficultyColor =
        when (challenge.difficulty.lowercase()) {
            "easy" -> MaterialTheme.colorScheme.tertiary
            "medium" -> MaterialTheme.colorScheme.secondary
            "hard" -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.error // fallback color
        }

    AnimatedVisibility(
        visible = true,
        enter =
            fadeIn(
                animationSpec =
                    tween(
                        durationMillis = 300,
                        delayMillis = index * 100,
                    ),
            ) +
                slideInVertically(
                    initialOffsetY = { it * 2 },
                    animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                ),
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .graphicsLayer {
                        scaleX = if (isPressed) 0.95f else 1f
                        scaleY = if (isPressed) 0.95f else 1f
                    }.pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                when {
                                    event.type == PointerEventType.Press -> isPressed = true
                                    event.type == PointerEventType.Release -> isPressed = false
                                }
                            }
                        }
                    },
            shape = RoundedCornerShape(24.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                ),
            onClick = { onClickChallenge(challenge) },
        ) {
            Box {
                // Background pattern
                Canvas(
                    modifier = Modifier.matchParentSize(),
                ) {
                    val pattern =
                        Path().apply {
                            moveTo(size.width * 0.8f, 0f)
                            cubicTo(
                                size.width,
                                size.height * 0.2f,
                                size.width * 0.8f,
                                size.height * 0.8f,
                                size.width,
                                size.height,
                            )
                        }

                    drawPath(
                        path = pattern,
                        color = difficultyColor.copy(alpha = 0.1f),
                        style = Stroke(width = 8f),
                    )
                }

                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ChallengeBadge((index + 1).toString())
                        DifficultyTag(
                            difficulty = challenge.difficulty,
                            color = difficultyColor,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = challenge.question,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun ChallengeBadge(id: String) {
    AssistChip(
        onClick = { },
        label = {
            Text(
                "Challenge $id",
                style = MaterialTheme.typography.labelMedium,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.EmojiEvents,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        },
    )
}

@Composable
fun DifficultyTag(
    difficulty: String,
    color: Color,
) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector =
                    when (difficulty.lowercase()) {
                        "easy" -> Icons.Rounded.Star
                        "medium" -> Icons.AutoMirrored.Rounded.StarHalf
                        else -> Icons.Rounded.StarBorder
                    },
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = color,
            )
            Text(
                text = difficulty,
                style = MaterialTheme.typography.labelMedium,
                color = color,
            )
        }
    }
}

@Composable
private fun LoadingAnimation() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier =
                Modifier
                    .size(64.dp)
                    .scale(1f),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 6.dp,
        )
    }
}

@Composable
private fun ErrorSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.error,
            )
            Text(
                "Challenge loading failed",
                style = MaterialTheme.typography.titleMedium,
            )
            Button(
                onClick = { /* Add retry logic */ },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                    ),
            ) {
                Text("Try Again")
            }
        }
    }
}
