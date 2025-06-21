package com.nyinyi.dailychallenge.ui.screens.play

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.data.model.Category
import com.nyinyi.dailychallenge.ui.screens.play.components.AnimatedCategoryText
import com.nyinyi.dailychallenge.ui.screens.play.components.DailyChallengeCard
import com.nyinyi.dailychallenge.ui.screens.play.components.GameMode
import com.nyinyi.dailychallenge.ui.screens.play.components.GameModesSection
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun PlayScreenPreview() {
    PlayScreenContent(
        onNavigateToGameMode = { /*TODO*/ },
        onNavigateToChallenge = {},
    )
}

@Composable
fun PlayScreenContent(
    viewModel: PlayScreenContentViewModel = koinViewModel(),
    onNavigateToChallenge: () -> Unit = {},
    onNavigateToGameMode: (GameMode) -> Unit,
) {
    var isContentVisible by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.categoryUpdateError) {
        uiState.categoryUpdateError?.let { errorMessage ->
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
            )
            viewModel.consumeCategoryUpdateError()
        }
    }

    LaunchedEffect(Unit) {
        isContentVisible = true
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
        ) {
            AnimatedVisibility(
                visible = isContentVisible,
                enter =
                    slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(durationMillis = 500),
                    ) + fadeIn(animationSpec = tween(durationMillis = 500)),
            ) {
                TopSection(viewModel = viewModel)
            }

            AnimatedVisibility(
                visible = isContentVisible,
                enter =
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec =
                            tween(
                                durationMillis = 500,
                                delayMillis = 100,
                                easing = EaseOutBack,
                            ),
                    ) +
                        fadeIn(
                            animationSpec =
                                tween(
                                    durationMillis = 500,
                                    delayMillis = 100,
                                ),
                        ),
            ) {
                DailyChallengeCard(
                    onStartChallenge = {
                        onNavigateToChallenge()
                    },
                )
            }

            AnimatedVisibility(
                visible = isContentVisible,
                enter =
                    slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec =
                            tween(
                                durationMillis = 500,
                                delayMillis = 200,
                                easing = EaseOutBack,
                            ),
                    ) +
                        fadeIn(
                            animationSpec =
                                tween(
                                    durationMillis = 500,
                                    delayMillis = 200,
                                ),
                        ),
            ) {
                GameModesSection(
                    onGameModeSelected = { gameMode ->
                        onNavigateToGameMode(gameMode)
                    },
                )
            }
        }
    }
}

@Composable
private fun TopSection(viewModel: PlayScreenContentViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Header with Welcome Text
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
        ) {
            Column {
                Text(
                    text = "Hello Challenger! 👋",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Let's practice",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = " in",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    )
                    // Category Dropdown
                    Box(
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        AnimatedCategoryText(
                            selectedCategory = selectedCategory,
                            expanded = expanded,
                            onClick = { expanded = true },
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier =
                                Modifier
                                    .background(
                                        MaterialTheme.colorScheme.surface,
                                        RoundedCornerShape(16.dp),
                                    ).width(200.dp),
                        ) {
                            Category.values().toList().forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = category.name,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color =
                                                    if (category == selectedCategory) {
                                                        MaterialTheme.colorScheme.primary
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurface
                                                    },
                                            )

                                            if (category == selectedCategory) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Check,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(20.dp),
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        viewModel.updateCategory(category)
                                        expanded = false
                                    },
                                    colors =
                                        MenuDefaults.itemColors(
                                            textColor =
                                                if (category == selectedCategory) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    MaterialTheme.colorScheme.onSurface
                                                },
                                        ),
                                    modifier = Modifier.height(48.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
