package com.nyinyi.dailychallenge.ui.screens.play.quiz.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeColors
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeElevation
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeShapes
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeSpacing

@Composable
fun TinderStyleCard(
    card: QuizCard,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onDrag: (Float) -> Unit = {},
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val rotation by animateFloatAsState(
        targetValue = (offsetX / 50).coerceIn(-15f, 15f),
        label = "rotation",
    )

    OutlinedCard(
        modifier =
            Modifier
                .width(300.dp)
                .height(400.dp)
                .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
                .rotate(rotation)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            when {
                                offsetX > 100 -> onSwipeRight()
                                offsetX < -100 -> onSwipeLeft()
                            }
                            offsetX = 0f
                            offsetY = 0f
                            onDrag(0f)
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                            onDrag(offsetX)
                        },
                    )
                },
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = DailyChallengeElevation.high,
            ),
        shape = DailyChallengeShapes.large,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(DailyChallengeSpacing.large),
            contentAlignment = Alignment.Center,
        ) {
            // Question Text
            Text(
                text = card.question,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            // Swipe Indicators overlaid on card
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = DailyChallengeSpacing.large),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                AnimatedVisibility(
                    visible = offsetX < 0,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Card(
                        colors =
                            CardDefaults.cardColors(
                                containerColor = DailyChallengeColors.danger.copy(alpha = 0.9f),
                            ),
                    ) {
                        Text(
                            text = "FALSE",
                            modifier = Modifier.padding(horizontal = DailyChallengeSpacing.large, vertical = DailyChallengeSpacing.small),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                AnimatedVisibility(
                    visible = offsetX > 0,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Card(
                        colors =
                            CardDefaults.cardColors(
                                containerColor = DailyChallengeColors.success.copy(alpha = 0.9f),
                            ),
                    ) {
                        Text(
                            text = "TRUE",
                            modifier = Modifier.padding(horizontal = DailyChallengeSpacing.large, vertical = DailyChallengeSpacing.small),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}
