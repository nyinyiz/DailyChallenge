package com.nyinyi.dailychallenge.ui.screens.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.ui.screens.play.PlayScreenContent
import com.nyinyi.dailychallenge.ui.screens.play.components.GameMode
import com.nyinyi.dailychallenge.ui.screens.profile.ProfileScreenContent
import com.nyinyi.dailychallenge.ui.theme.ThemeColors

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
                            text = "Daily Challenges",
                            style =
                                MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                ),
                            modifier = Modifier.padding(bottom = 8.dp),
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
