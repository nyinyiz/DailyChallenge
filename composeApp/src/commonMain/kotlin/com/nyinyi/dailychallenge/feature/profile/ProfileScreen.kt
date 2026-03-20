package com.nyinyi.dailychallenge.feature.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.data.model.UserProfile
import com.nyinyi.dailychallenge.feature.profile.components.AchievementItem
import com.nyinyi.dailychallenge.feature.profile.components.FailedExercisesSection
import com.nyinyi.dailychallenge.feature.profile.components.InfoRow
import com.nyinyi.dailychallenge.feature.profile.components.ProfileEditCard
import com.nyinyi.dailychallenge.feature.profile.components.ProfileHeader
import com.nyinyi.dailychallenge.feature.profile.components.SectionContainer
import com.nyinyi.dailychallenge.feature.profile.components.StatCard
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeShapes
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeSpacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
                                    MaterialTheme.colorScheme.background,
                                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.18f),
                                ),
                        ),
                ),
        contentAlignment = Alignment.Center,
    ) {
        when (val state = uiState) {
            is ProfileUiState.Loading -> CircularProgressIndicator()
            is ProfileUiState.Error -> ErrorMessage(state.message)
            is ProfileUiState.Success ->
                ProfileScreenBody(
                    userProfile = state.userProfile,
                    editorState = state.editorState,
                    dashboard = state.dashboard,
                    onNameChanged = viewModel::onEditorNameChanged,
                    onEmailChanged = viewModel::onEditorEmailChanged,
                    onCancelEdit = viewModel::resetEditor,
                    onSaveEdit = viewModel::saveProfileEdits,
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
private fun ProfileScreenBody(
    userProfile: UserProfile,
    editorState: ProfileEditorState,
    dashboard: ProfileDashboard,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onDarkThemeChanged: (Boolean) -> Unit,
    onClearFailedExercises: () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val wideLayout = maxWidth >= 920.dp

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding =
                PaddingValues(
                    horizontal = if (wideLayout) 24.dp else 16.dp,
                    vertical = 20.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.sectionSpacing),
        ) {
            item {
                ProfileHeader(
                    name = dashboard.displayName,
                    email = userProfile.email,
                    levelLabel = dashboard.levelLabel,
                    levelProgress = dashboard.levelProgress,
                    currentGoal = dashboard.currentGoal,
                    reviewQueueCount = dashboard.reviewQueueCount,
                    achievementCount = dashboard.achievementCount,
                    accuracyRate = dashboard.accuracyRate,
                    initials = dashboard.initials,
                )
            }

            item {
                StatsSection(dashboard = dashboard, wideLayout = wideLayout)
            }

            if (wideLayout) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.sectionSpacing),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.sectionSpacing),
                        ) {
                            LearningProgressSection(dashboard = dashboard)
                            AchievementsSection(dashboard = dashboard)
                            FailedExercisesSection(
                                userProfile = userProfile,
                                onClearFailedExercises = onClearFailedExercises,
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.sectionSpacing),
                        ) {
                            AccountSection(
                                editorState = editorState,
                                userProfile = userProfile,
                                onNameChanged = onNameChanged,
                                onEmailChanged = onEmailChanged,
                                onCancelEdit = onCancelEdit,
                                onSaveEdit = onSaveEdit,
                            )
                            ModernThemePreference(
                                darkTheme = userProfile.darkTheme,
                                onDarkThemeChanged = onDarkThemeChanged,
                            )
                        }
                    }
                }
            } else {
                item {
                    LearningProgressSection(dashboard = dashboard)
                }
                item {
                    AccountSection(
                        editorState = editorState,
                        userProfile = userProfile,
                        onNameChanged = onNameChanged,
                        onEmailChanged = onEmailChanged,
                        onCancelEdit = onCancelEdit,
                        onSaveEdit = onSaveEdit,
                    )
                }
                item {
                    AchievementsSection(dashboard = dashboard)
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
    }
}

@Composable
private fun StatsSection(
    dashboard: ProfileDashboard,
    wideLayout: Boolean,
) {
    if (wideLayout) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.large),
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Streak",
                value = "${dashboard.streakDays} days",
                icon = Icons.Rounded.LocalFireDepartment,
                accentColor = MaterialTheme.colorScheme.primary,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Progress",
                value = dashboard.completedChallenges.toString(),
                icon = Icons.Rounded.AutoGraph,
                accentColor = MaterialTheme.colorScheme.secondary,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Achievements",
                value = dashboard.achievementCount.toString(),
                icon = Icons.Rounded.EmojiEvents,
                accentColor = MaterialTheme.colorScheme.tertiary,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Accuracy",
                value = "${dashboard.accuracyRate}%",
                icon = Icons.Rounded.Star,
                accentColor = MaterialTheme.colorScheme.primary,
            )
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.large)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.large),
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Streak",
                    value = "${dashboard.streakDays} days",
                    icon = Icons.Rounded.LocalFireDepartment,
                    accentColor = MaterialTheme.colorScheme.primary,
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Progress",
                    value = dashboard.completedChallenges.toString(),
                    icon = Icons.Rounded.AutoGraph,
                    accentColor = MaterialTheme.colorScheme.secondary,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.large),
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Achievements",
                    value = dashboard.achievementCount.toString(),
                    icon = Icons.Rounded.EmojiEvents,
                    accentColor = MaterialTheme.colorScheme.tertiary,
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Accuracy",
                    value = "${dashboard.accuracyRate}%",
                    icon = Icons.Rounded.Star,
                    accentColor = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun LearningProgressSection(dashboard: ProfileDashboard) {
    SectionContainer(
        title = "Learning snapshot",
    ) {
        WeeklyActivityChart(activity = dashboard.weeklyActivity)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.medium),
        ) {
            dashboard.focusAreas.take(2).forEach { point ->
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = com.nyinyi.dailychallenge.ui.theme.DailyChallengeShapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.32f),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = point.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = point.value,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeeklyActivityChart(activity: List<ActivitySummary>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        activity.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(width = 30.dp, height = (34 + (item.sessions * 12)).dp)
                            .aspectRatio(0.52f, matchHeightConstraintsFirst = true)
                            .background(
                                color =
                                    if (item.sessions > 0) {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.78f)
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                shape = DailyChallengeShapes.medium,
                            ),
                )
                Text(
                    text = item.dayLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun AchievementsSection(dashboard: ProfileDashboard) {
    SectionContainer(
        title = "Achievements",
    ) {
        dashboard.achievements.forEach { achievement ->
            AchievementItem(
                title = achievement.title,
                description = achievement.description,
                unlocked = achievement.unlocked,
            )
        }
    }
}

@Composable
private fun AccountSection(
    editorState: ProfileEditorState,
    userProfile: UserProfile,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: () -> Unit,
) {
    SectionContainer(
        title = "Account",
        showDivider = true,
    ) {
        Surface(
            shape = com.nyinyi.dailychallenge.ui.theme.DailyChallengeShapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.32f),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = userProfile.name.ifBlank { "Challenger" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = userProfile.email.ifBlank { "Local profile" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        ProfileEditCard(
            editorState = editorState,
            onNameChanged = onNameChanged,
            onEmailChanged = onEmailChanged,
            onCancel = onCancelEdit,
            onConfirm = onSaveEdit,
        )
    }
}

@Composable
private fun ModernThemePreference(
    darkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
) {
    SectionContainer(
        title = "Appearance",
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DailyChallengeSpacing.large),
        ) {
            ThemeOptionCard(
                modifier = Modifier.weight(1f),
                title = "Light",
                icon = Icons.Rounded.LightMode,
                isSelected = !darkTheme,
                supportingText = "Bright and airy",
                onClick = { onDarkThemeChanged(false) },
            )
            ThemeOptionCard(
                modifier = Modifier.weight(1f),
                title = "Dark",
                icon = Icons.Rounded.DarkMode,
                isSelected = darkTheme,
                supportingText = "Focused and calm",
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
    supportingText: String,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue =
            if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f)
            } else {
                MaterialTheme.colorScheme.surface
            },
        label = "themeCardBackground",
    )
    val borderColor by animateColorAsState(
        targetValue =
            if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            },
        label = "themeCardBorder",
    )

    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = DailyChallengeShapes.large,
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (isSelected) {
                Surface(
                    shape = com.nyinyi.dailychallenge.ui.theme.DailyChallengeShapes.small,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                ) {
                    Text(
                        text = "Active",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
