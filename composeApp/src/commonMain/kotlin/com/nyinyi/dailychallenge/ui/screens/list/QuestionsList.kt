package com.nyinyi.dailychallenge.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.ui.screens.play.PlayScreenContent
import com.nyinyi.dailychallenge.ui.screens.play.components.GameMode
import com.nyinyi.dailychallenge.ui.screens.profile.ProfileScreenContent
import com.nyinyi.dailychallenge.ui.theme.ThemeColors
import org.koin.compose.viewmodel.koinViewModel

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Home")

    object Play : BottomNavItem("play", Icons.Filled.PlayArrow, "Play")

    object Profile : BottomNavItem("profile", Icons.Filled.Person, "Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsList(
    onClickChallenge: (DailyChallengeObj) -> Unit = {},
    onToggleTheme: () -> Unit = {},
    navigateToGameMode: (GameMode) -> Unit = {},
) {
    var currentScreen by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    Scaffold(
        topBar = {
            if (currentScreen == BottomNavItem.Home) {
                TopAppBar(
                    title = {
                        Text(
                            when (currentScreen) {
                                BottomNavItem.Home -> "Daily Challenges"
                                BottomNavItem.Play -> "Play Zone"
                                BottomNavItem.Profile -> "Your Profile"
                            },
                            style =
                                MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                ),
                        )
                    },
                    actions = {
                        IconButton(onClick = onToggleTheme) {
                            Text(
                                text = if (ThemeColors.isDarkTheme) "☀️" else "🌙",
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                )
            }
        },
        bottomBar = {
            QuestionsListBottomNavigationBar(
                currentScreen = currentScreen,
                onScreenSelected = { screen -> currentScreen = screen },
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                BottomNavItem.Home ->
                    DailyChallengeListContent(
                        onClickChallenge = onClickChallenge,
                    )

                BottomNavItem.Play ->
                    PlayScreenContent(
                        onNavigateToGameMode = { gameMode ->
                            navigateToGameMode(gameMode)
                        },
                        onNavigateToChallenge = {
                            // TODO navigate to challenge
                        },
                    )

                BottomNavItem.Profile -> ProfileScreenContent()
            }
        }
    }
}

@Composable
fun QuestionsListBottomNavigationBar(
    currentScreen: BottomNavItem,
    onScreenSelected: (BottomNavItem) -> Unit,
) {
    val items =
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Play,
            BottomNavItem.Profile,
        )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface, // Or any other color
        contentColor = MaterialTheme.colorScheme.onSurface, // Default color for icons and text
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentScreen == screen,
                onClick = { onScreenSelected(screen) },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer, // Color of the selection indicator
                    ),
            )
        }
    }
}

@Composable
fun DailyChallengeListContent(
    viewModel: QuestionListViewModel = koinViewModel(),
    onClickChallenge: (DailyChallengeObj) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    @Composable
    fun getDifferentColor(difficulty: String): Color =
        when (difficulty) {
            "Easy" -> MaterialTheme.colorScheme.tertiary
            "Medium" -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.error
        }

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (val state = uiState) {
            is QuestionListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            is QuestionListState.Success -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    itemsIndexed(state.dailyChallenges) { _, challenge ->
                        AnimatedVisibility(
                            visible = true,
                            enter =
                                fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                                    scaleIn(
                                        initialScale = 0.92f,
                                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                                    ),
                            exit = fadeOut(),
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors =
                                    CardDefaults.cardColors(
                                        containerColor =
                                            MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                2.dp,
                                            ),
                                    ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                onClick = {
                                    onClickChallenge(challenge)
                                },
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        AssistChip(
                                            onClick = { /* No action needed for the chip itself */ },
                                            label = { Text("Challenge ${challenge.id}") },
                                        )
                                        Box(
                                            modifier =
                                                Modifier
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(
                                                        getDifferentColor(challenge.difficulty).copy(
                                                            alpha = 0.2f,
                                                        ),
                                                    ).padding(horizontal = 8.dp, vertical = 2.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                text = challenge.difficulty,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = getDifferentColor(challenge.difficulty),
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        challenge.question,
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            is QuestionListState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Challenge loading failed. Please try again.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}
