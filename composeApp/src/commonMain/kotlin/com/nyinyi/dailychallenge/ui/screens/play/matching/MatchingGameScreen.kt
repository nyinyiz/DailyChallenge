package com.nyinyi.dailychallenge.ui.screens.play.matching

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.ui.screens.play.matching.components.MatchingGameResultScreen
import com.nyinyi.dailychallenge.ui.screens.play.matching.components.MatchingItem
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.EmptyState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.ErrorState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.LoadingState
import com.nyinyi.dailychallenge.ui.screens.play.quiz.components.QuestionProgressUI
import com.nyinyi.dailychallenge.ui.theme.ThemeColors
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchingGameScreen(
    onBack: () -> Unit,
    onBackToHome: () -> Unit,
    onToggleTheme: () -> Unit,
    viewModel: MatchingGameViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Matching Game",
                        style =
                            MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIosNew,
                            contentDescription = "Back",
                        )
                    }
                },
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
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            when (val state = uiState) {
                is MatchingGameUiState.Loading -> LoadingState()
                is MatchingGameUiState.Error ->
                    ErrorState(
                        message = state.message,
                        onRetry = viewModel::loadQuestions,
                    )

                is MatchingGameUiState.Empty ->
                    EmptyState(
                        onRetry = viewModel::loadQuestions,
                    )

                is MatchingGameUiState.Game -> {
                    val difficultStatus =
                        if (state.currentQuestion?.difficulty != null && state.currentQuestion.difficulty.isNotEmpty()) {
                            state.currentQuestion.difficulty.lowercase()
                        } else {
                            "completed"
                        }

                    QuestionProgressUI(
                        difficulty = difficultStatus,
                        currentQuestion = state.currentQuestionIndex + 1,
                        totalQuestions = state.totalQuestions,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    )

                    if (state.isGameComplete) {
                        viewModel.getGameResult()?.let { result ->
                            MatchingGameResultScreen(
                                result = result,
                                onRestartGame = viewModel::restartGame,
                                onBackToHome = onBackToHome,
                            )
                        }
                    } else {
                        GameContent(
                            gameState = state,
                            onLeftItemClick = viewModel::selectLeftItem,
                            onRightItemClick = viewModel::selectRightItem,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameContent(
    gameState: MatchingGameUiState.Game,
    onLeftItemClick: (Int) -> Unit,
    onRightItemClick: (Int) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    var showMatchFeedback by remember { mutableStateOf<MatchFeedback?>(null) }
    var showInstructions by remember { mutableStateOf(false) }

    // Clear feedback after delay
    LaunchedEffect(showMatchFeedback) {
        if (showMatchFeedback != null) {
            delay(800)
            showMatchFeedback = null
        }
    }

    // Check if a match was just made
    LaunchedEffect(gameState.matchedPairs.size) {
        if (gameState.matchedPairs.isNotEmpty()) {
            showMatchFeedback = MatchFeedback.Correct
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            gameState.currentQuestion?.let { question ->
                // Question Title Card with Help Icon
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = question.question,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.weight(1f),
                        )

                        IconButton(
                            onClick = { showInstructions = !showInstructions },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                                contentDescription = "Show instructions",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Instructions Card (collapsible)
                AnimatedVisibility(
                    visible = showInstructions,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                ) {
                    Column {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme.colorScheme.secondaryContainer.copy(
                                            alpha = 0.5f,
                                        ),
                                ),
                        ) {
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.SwapHoriz,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp),
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = "Tap one item from each column to match pairs",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Progress and Attempts Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Progress indicator with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "${gameState.matchedPairs.size} / ${gameState.leftItems.size} matched",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    // Incorrect attempts warning
                    if (gameState.incorrectAttempts > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                text = "${gameState.incorrectAttempts} wrong",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Visual progress bar
                LinearProgressIndicator(
                    progress = { gameState.matchedPairs.size.toFloat() / gameState.leftItems.size.toFloat() },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Column Headers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "Column A",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                    )

                    Icon(
                        imageVector = Icons.Rounded.SwapHoriz,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp),
                    )

                    Text(
                        text = "Column B",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Matching Columns
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Left Column
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        itemsIndexed(
                            items = gameState.leftItems,
                            key = { index, _ -> "left_$index" },
                        ) { index, item ->
                            MatchingItem(
                                text = item,
                                isSelected = gameState.selectedLeftIndex == index,
                                isMatched = gameState.matchedPairs.any { it.first == index },
                                onClick = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onLeftItemClick(index)
                                },
                            )
                        }
                    }

                    // Center Divider
                    Box(
                        modifier =
                            Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(
                                    MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(1.dp),
                                ).align(Alignment.CenterVertically),
                    )

                    // Right Column
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        itemsIndexed(
                            items = gameState.rightItems,
                            key = { index, _ -> "right_$index" },
                        ) { index, item ->
                            MatchingItem(
                                text = item,
                                isSelected = gameState.selectedRightIndex == index,
                                isMatched = gameState.matchedPairs.any { it.second == index },
                                onClick = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onRightItemClick(index)
                                },
                            )
                        }
                    }
                }
            }
        }

        // Match Feedback Overlay
        AnimatedVisibility(
            visible = showMatchFeedback != null,
            enter =
                fadeIn() +
                    scaleIn(
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                    ),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.Center),
        ) {
            showMatchFeedback?.let { feedback ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shadowElevation = 8.dp,
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp),
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Text(
                            text = "Perfect Match! ✨",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }
        }
    }
}

private enum class MatchFeedback {
    Correct,
}
