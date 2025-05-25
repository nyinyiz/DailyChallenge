package com.nyinyi.dailychallenge.ui.screens.play.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Compare
import androidx.compose.material.icons.rounded.LibraryAddCheck
import androidx.compose.material.icons.rounded.RadioButtonChecked
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

sealed class GameMode(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val backgroundColor: List<Color>,
) {
    object TrueOrFalse : GameMode(
        "True or False",
        "Test your knowledge with yes/no questions",
        Icons.Rounded.CheckCircle,
        listOf(Color(0xFF4CAF50), Color(0xFF2E7D32)), // Green shades
    )

    object MultipleChoice : GameMode(
        "Multiple Choice",
        "Select the correct answer from options",
        Icons.Rounded.RadioButtonChecked,
        listOf(Color(0xFF2196F3), Color(0xFF1565C0)), // Blue shades
    )

    object MultipleSelect : GameMode(
        "Multiple Select",
        "Choose all correct answers",
        Icons.Rounded.LibraryAddCheck,
        listOf(Color(0xFF9C27B0), Color(0xFF6A1B9A)), // Purple shades
    )

    object MatchingGame : GameMode(
        "Matching Game",
        "Match related pairs correctly",
        Icons.Rounded.Compare,
        listOf(Color(0xFFFF9800), Color(0xFFF57C00)), // Orange shades
    )
}

@Composable
fun GameModesSection(onGameModeSelected: (GameMode) -> Unit) {
    val gameModeList =
        listOf(
            GameMode.TrueOrFalse,
            GameMode.MultipleChoice,
            GameMode.MultipleSelect,
            GameMode.MatchingGame,
        )
    Column(
        modifier = Modifier.padding(24.dp),
    ) {
        Text(
            text = "Game Modes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = 120.dp),
            modifier = Modifier.fillMaxSize(),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = gameModeList,
            ) { gameMode ->
                GameModeCard(
                    gameMode = gameMode,
                    onSelected = { onGameModeSelected(gameMode) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameModeCard(
    gameMode: GameMode,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPressed by remember { mutableStateOf(false) }

    ElevatedCard(
        onClick = {
            isPressed = true
            onSelected()
        },
        modifier =
            modifier
                .defaultMinSize(minHeight = 200.dp)
                .graphicsLayer {
                    scaleX = if (isPressed) 0.95f else 1f
                    scaleY = if (isPressed) 0.95f else 1f
                }.animateContentSize(
                    animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                ),
        shape = RoundedCornerShape(24.dp),
        elevation =
            CardDefaults.elevatedCardElevation(
                defaultElevation = 8.dp,
                pressedElevation = 4.dp,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(gameMode.backgroundColor),
                    ),
        ) {
            // Decorative circles in background
            Box(
                modifier =
                    Modifier
                        .size(100.dp)
                        .offset(x = (-20).dp, y = (-20).dp)
                        .background(
                            Color.White.copy(alpha = 0.1f),
                            shape = CircleShape,
                        ),
            )
            Box(
                modifier =
                    Modifier
                        .size(60.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 20.dp, y = 20.dp)
                        .background(
                            Color.White.copy(alpha = 0.1f),
                            shape = CircleShape,
                        ),
            )

            // Content
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // Icon Section
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(56.dp),
                ) {
                    Icon(
                        imageVector = gameMode.icon,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .padding(12.dp)
                                .size(32.dp),
                        tint = Color.White,
                    )
                }

                // Text Section
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    Text(
                        text = gameMode.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = gameMode.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                    )

                    Row(
                        modifier =
                            Modifier
                                .padding(top = 12.dp)
                                .background(
                                    Color.White.copy(alpha = 0.15f),
                                    RoundedCornerShape(8.dp),
                                ).padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White,
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.StarHalf,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White,
                        )
                        Icon(
                            imageVector = Icons.Rounded.StarBorder,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }

    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}
