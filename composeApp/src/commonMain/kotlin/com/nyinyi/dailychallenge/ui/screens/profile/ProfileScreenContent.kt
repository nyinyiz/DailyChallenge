package com.nyinyi.dailychallenge.ui.screens.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val isDesktop = maxWidth >= 800.dp

        if (isDesktop) {
            DesktopLayout(
                userProfile = userProfile,
                onNameChanged = onNameChanged,
                onEmailChanged = onEmailChanged,
                onDarkThemeChanged = onDarkThemeChanged,
                onClearFailedExercises = onClearFailedExercises,
            )
        } else {
            MobileLayout(
                userProfile = userProfile,
                onNameChanged = onNameChanged,
                onEmailChanged = onEmailChanged,
                onDarkThemeChanged = onDarkThemeChanged,
                onClearFailedExercises = onClearFailedExercises,
            )
        }
    }
}

@Composable
private fun MobileLayout(
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
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item {
            ProfileHeader()
        }
        item {
            ProfileEditCard(
                userProfile = userProfile,
                onNameChanged = onNameChanged,
                onEmailChanged = onEmailChanged,
            )
        }

        item {
            ModernThemePreference(
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
private fun DesktopLayout(
    userProfile: UserProfile,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onDarkThemeChanged: (Boolean) -> Unit,
    onClearFailedExercises: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
    ) {
        ProfileHeader()
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // Left Column: Profile Info
            Column(
                modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                ProfileEditCard(
                    userProfile = userProfile,
                    onNameChanged = onNameChanged,
                    onEmailChanged = onEmailChanged,
                )
            }

            // Right Column: Settings & Stats
            Column(
                modifier = Modifier.weight(0.6f),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                ModernThemePreference(
                    darkTheme = userProfile.darkTheme,
                    onDarkThemeChanged = onDarkThemeChanged,
                )

                FailedExercisesSection(
                    userProfile = userProfile,
                    onClearFailedExercises = onClearFailedExercises,
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Manage your account and preferences",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun ModernThemePreference(
    darkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Appearance",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ThemeOptionCard(
                modifier = Modifier.weight(1f),
                title = "Light Mode",
                icon = Icons.Rounded.LightMode,
                isSelected = !darkTheme,
                onClick = { onDarkThemeChanged(false) },
            )

            ThemeOptionCard(
                modifier = Modifier.weight(1f),
                title = "Dark Mode",
                icon = Icons.Rounded.DarkMode,
                isSelected = darkTheme,
                onClick = { onDarkThemeChanged(true) },
            )
        }
    }
}

@Composable
private fun ThemeOptionCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue =
            if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        label = "cardBg",
    )

    val borderColor by animateColorAsState(
        targetValue =
            if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            },
        label = "cardBorder",
    )

    val iconColor by animateColorAsState(
        targetValue =
            if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        label = "iconColor",
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        label = "scale",
    )

    Surface(
        modifier =
            modifier
                .height(100.dp)
                .scale(scale)
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(2.dp, borderColor),
        shadowElevation = if (isSelected) 4.dp else 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
            )
        }
    }
}
