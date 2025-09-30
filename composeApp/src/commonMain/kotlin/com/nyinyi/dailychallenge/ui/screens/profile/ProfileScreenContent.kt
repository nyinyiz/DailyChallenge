package com.nyinyi.dailychallenge.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.data.model.UserProfile
import com.nyinyi.dailychallenge.ui.screens.profile.components.FailedExercisesSection
import com.nyinyi.dailychallenge.ui.screens.profile.components.ProfileEditCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenContent(viewModel: ProfileViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (val state = uiState) {
            is ProfileUiState.Loading -> CircularProgressIndicator()
            is ProfileUiState.Error -> ErrorMessage(state.message)
            is ProfileUiState.Success ->
                ProfileContent(
                    userProfile = state.userProfile,
                    onNameChanged = viewModel::updateUserName,
                    onEmailChanged = viewModel::updateUserEmail,
                    onDarkThemeChanged = viewModel::updateDarkTheme,
                    onClearFailedExercises = viewModel::clearFailedExercises,
                )
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = "Error: $message",
        color = MaterialTheme.colorScheme.error,
    )
}

@Composable
private fun ProfileContent(
    userProfile: UserProfile,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onDarkThemeChanged: (Boolean) -> Unit,
    onClearFailedExercises: () -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ProfileHeader()
            ProfileEditCard(
                userProfile = userProfile,
                onNameChanged = onNameChanged,
                onEmailChanged = onEmailChanged,
            )
        }

        item {
            ThemePreferenceCard(
                darkTheme = userProfile.darkTheme,
                onDarkThemeChanged = onDarkThemeChanged,
            )
        }

        item {
            FailedExercisesSection(
                userProfile = userProfile,
                onClearFailedExercises = onClearFailedExercises,
            )
        }
    }
}

@Composable
private fun ProfileHeader() {
    Text(
        text = "My Profile",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ThemePreferenceCard(
    darkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        imageVector =
                            if (darkTheme) {
                                Icons.Rounded.DarkMode
                            } else {
                                Icons.Rounded.LightMode
                            },
                        contentDescription = "Theme icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                    )

                    Column {
                        Text(
                            text = if (darkTheme) "Dark Mode" else "Light Mode",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = if (darkTheme) "Switch to light theme" else "Switch to dark theme",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Switch(
                    checked = darkTheme,
                    onCheckedChange = onDarkThemeChanged,
                )
            }
        }
    }
}
