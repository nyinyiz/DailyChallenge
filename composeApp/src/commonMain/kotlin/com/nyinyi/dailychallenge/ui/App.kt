package com.nyinyi.dailychallenge.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.nyinyi.dailychallenge.navigation.AppNavigation
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(viewModel: AppViewModel = koinViewModel()) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle(
        initialValue =
            com.nyinyi.dailychallenge.data.model.UserProfile(
                darkTheme = true,
            ),
    )
    val navController = rememberNavController()

    DailyChallengeTheme(darkTheme = userProfile.darkTheme) {
        AppNavigation(
            navController = navController,
            viewModel = viewModel,
            darkTheme = userProfile.darkTheme,
            onToggleTheme = { },
        )
    }
}
