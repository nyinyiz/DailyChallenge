package com.nyinyi.dailychallenge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Define composition local to make dark theme state accessible throughout the app
val LocalDarkTheme = compositionLocalOf { false }

// Light mode colors with moonLight as primary
private val LightColors =
    lightColorScheme(
        primary = Color(0xFF7B9ECF), // Soft blue (moonLight primary)
        onPrimary = Color.White,
        primaryContainer = Color(0xFFDDE9F6), // Very light blue
        onPrimaryContainer = Color(0xFF0A2D56), // Dark blue for contrast
        secondary = Color(0xFF9BBBEA), // Slightly lighter blue
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE0EAF7), // Very light blue with a hint of purple
        onSecondaryContainer = Color(0xFF1A365F), // Dark blue for contrast
        tertiary = Color(0xFFB0C4E8), // Light cool blue
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFEAF0FA), // Very light blue, almost white
        onTertiaryContainer = Color(0xFF2A4676), // Dark blue for contrast
        background = Color(0xFFF8FBFF), // Almost white with slight blue tint
        onBackground = Color(0xFF1A1C1E), // Very dark blue, almost black
        surface = Color(0xFFF8FBFF), // Almost white with slight blue tint
        onSurface = Color(0xFF1A1C1E), // Very dark blue, almost black
        surfaceVariant = Color(0xFFE5E8EC), // Light gray with blue tint
        onSurfaceVariant = Color(0xFF424547), // Dark gray
        error = Color(0xFFBA1A1A), // Standard error red
        onError = Color.White,
    )

// Dark mode colors with darkBlue as primary
private val DarkColors =
    darkColorScheme(
        primary = Color(0xFF3B6EA8), // Dark blue primary
        onPrimary = Color.White,
        primaryContainer = Color(0xFF1F3A59), // Deeper blue
        onPrimaryContainer = Color(0xFFAFD2FF), // Light blue
        secondary = Color(0xFF486B9E), // Mid-tone blue
        onSecondary = Color.White,
        secondaryContainer = Color(0xFF25374F), // Deep blue with a hint of gray
        onSecondaryContainer = Color(0xFFBDD4F5), // Light blue
        tertiary = Color(0xFF2C4A7C), // Mid dark blue
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFF1A2E4F), // Very dark blue
        onTertiaryContainer = Color(0xFFD1E4FF), // Very light blue
        background = Color(0xFF121A24), // Very dark blue-gray
        onBackground = Color(0xFFE3E3E3), // Off-white
        surface = Color(0xFF121A24), // Very dark blue-gray
        onSurface = Color(0xFFE3E3E3), // Off-white
        surfaceVariant = Color(0xFF202C3B), // Dark blue-gray
        onSurfaceVariant = Color(0xFFBFC9D9), // Light gray-blue
        error = Color(0xFFFFB4AB), // Lighter red for dark theme
        onError = Color(0xFF690005), // Dark red
    )

@Composable
fun DailyChallengeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(LocalDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}

// Get current theme info
object ThemeColors {
    val current: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    val isDarkTheme: Boolean
        @Composable
        get() = LocalDarkTheme.current
}
