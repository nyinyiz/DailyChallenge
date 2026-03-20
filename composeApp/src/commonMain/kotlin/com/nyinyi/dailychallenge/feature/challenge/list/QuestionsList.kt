package com.nyinyi.dailychallenge.feature.challenge.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.feature.profile.ProfileScreen
import com.nyinyi.dailychallenge.feature.play.home.PlayHubScreen
import com.nyinyi.dailychallenge.feature.play.home.components.PlayMode
import org.koin.compose.viewmodel.koinViewModel

enum class AppTab(
    val icon: ImageVector,
    val label: String,
) {
    Home(Icons.Filled.Home, "Home"),
    List(Icons.AutoMirrored.Filled.List, "Questions"),
    Profile(Icons.Filled.Person, "Profile"),
}

@Composable
fun HomeTabScreen(
    onTabSelected: (AppTab) -> Unit,
    onClickChallenge: (DailyChallengeObj) -> Unit,
    navigateToPlayMode: (PlayMode) -> Unit,
    viewModel: QuestionListViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is QuestionListEvent.RandomDataChanged -> onClickChallenge(event.randomChallenges)
            }
        }
    }

    AppTabScaffold(
        currentTab = AppTab.Home,
        onTabSelected = onTabSelected,
    ) {
        PlayHubScreen(
            onNavigateToPlayMode = navigateToPlayMode,
            onNavigateToChallenge = viewModel::getRandomChallenges,
        )
    }
}

@Composable
fun ChallengeListTabScreen(
    onTabSelected: (AppTab) -> Unit,
    onClickChallenge: (DailyChallengeObj) -> Unit,
    viewModel: QuestionListViewModel = koinViewModel(),
) {
    AppTabScaffold(
        currentTab = AppTab.List,
        onTabSelected = onTabSelected,
    ) {
        ChallengeListScreen(
            viewModel = viewModel,
            onClickChallenge = onClickChallenge,
        )
    }
}

@Composable
fun ProfileTabScreen(
    onTabSelected: (AppTab) -> Unit,
) {
    AppTabScaffold(
        currentTab = AppTab.Profile,
        onTabSelected = onTabSelected,
    ) {
        ProfileScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTabScaffold(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            if (currentTab == AppTab.List) {
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
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                )
            }
        },
        bottomBar = {
            QuestionsListBottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = onTabSelected,
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}

@Composable
fun QuestionsListBottomNavigationBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        AppTab.entries.forEach { tab ->
            NavigationBarItem(
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) },
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
            )
        }
    }
}
