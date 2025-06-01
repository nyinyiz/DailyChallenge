package com.nyinyi.dailychallenge.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.nyinyi.dailychallenge.navigation.AppNavigation
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(viewModel: AppViewModel = koinViewModel()) {
    var darkTheme by rememberSaveable { mutableStateOf(true) }
    val navController = rememberNavController()

    DailyChallengeTheme(darkTheme = darkTheme) {
        AppNavigation(
            navController = navController,
            viewModel = viewModel,
            darkTheme = darkTheme,
            onToggleTheme = { darkTheme = !darkTheme },
        )
    }
}
