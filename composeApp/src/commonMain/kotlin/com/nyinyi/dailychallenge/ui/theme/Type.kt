package com.nyinyi.dailychallenge.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun rememberResponsiveTypography(): Typography {
    val windowSize = rememberWindowSizeClass()

    return when (windowSize) {
        WindowSizeClass.Compact -> SmallScreenTypography
        WindowSizeClass.Medium -> TabletTypography
        WindowSizeClass.Expanded -> DesktopTypography
    }
}

val SmallScreenTypography =
    Typography(
        displayLarge =
            TextStyle(
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = (-0.25).sp,
                fontWeight = FontWeight.Normal,
            ),
        displayMedium =
            TextStyle(
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Normal,
            ),
        displaySmall =
            TextStyle(
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Normal,
            ),
        headlineLarge =
            TextStyle(
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        headlineMedium =
            TextStyle(
                fontSize = 20.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        headlineSmall =
            TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        titleLarge =
            TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Medium,
            ),
        titleMedium =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.15.sp,
                fontWeight = FontWeight.Medium,
            ),
        titleSmall =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.1.sp,
                fontWeight = FontWeight.Medium,
            ),
        bodyLarge =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodyMedium =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.25.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodySmall =
            TextStyle(
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
                fontWeight = FontWeight.Normal,
            ),
        labelLarge =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.1.sp,
                fontWeight = FontWeight.Medium,
            ),
        labelMedium =
            TextStyle(
                fontSize = 11.sp,
                lineHeight = 14.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Medium,
            ),
        labelSmall =
            TextStyle(
                fontSize = 10.sp,
                lineHeight = 14.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Medium,
            ),
    )

val TabletTypography =
    Typography(
        displayLarge =
            TextStyle(
                fontSize = 48.sp,
                lineHeight = 56.sp,
                letterSpacing = (-0.25).sp,
                fontWeight = FontWeight.Normal,
            ),
        displayMedium =
            TextStyle(
                fontSize = 42.sp,
                lineHeight = 50.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Normal,
            ),
        displaySmall =
            TextStyle(
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Normal,
            ),
        headlineLarge =
            TextStyle(
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        headlineMedium =
            TextStyle(
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        headlineSmall =
            TextStyle(
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        titleLarge =
            TextStyle(
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Medium,
            ),
        titleMedium =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp,
                fontWeight = FontWeight.Medium,
            ),
        titleSmall =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
                fontWeight = FontWeight.Medium,
            ),
        bodyLarge =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodyMedium =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodySmall =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.4.sp,
                fontWeight = FontWeight.Normal,
            ),
        labelLarge =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
                fontWeight = FontWeight.Medium,
            ),
        labelMedium =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Medium,
            ),
        labelSmall =
            TextStyle(
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Medium,
            ),
    )

val DesktopTypography =
    Typography(
        displayLarge =
            TextStyle(
                fontSize = 57.sp,
                lineHeight = 64.sp,
                letterSpacing = (-0.25).sp,
                fontWeight = FontWeight.Normal,
            ),
        displayMedium =
            TextStyle(
                fontSize = 48.sp,
                lineHeight = 56.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Normal,
            ),
        displaySmall =
            TextStyle(
                fontSize = 42.sp,
                lineHeight = 50.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Normal,
            ),
        headlineLarge =
            TextStyle(
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        headlineMedium =
            TextStyle(
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        headlineSmall =
            TextStyle(
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        titleLarge =
            TextStyle(
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Medium,
            ),
        titleMedium =
            TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.15.sp,
                fontWeight = FontWeight.Medium,
            ),
        titleSmall =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.1.sp,
                fontWeight = FontWeight.Medium,
            ),
        bodyLarge =
            TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodyMedium =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.25.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodySmall =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.4.sp,
                fontWeight = FontWeight.Normal,
            ),
        labelLarge =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.1.sp,
                fontWeight = FontWeight.Medium,
            ),
        labelMedium =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Medium,
            ),
        labelSmall =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Medium,
            ),
    )

@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val windowInfo = LocalWindowInfo.current
    val containerWidth = windowInfo.containerSize.width.dp

    println("Container Width: $containerWidth")
    return remember(containerWidth) {
        when {
            containerWidth < 1000.dp -> WindowSizeClass.Compact // Most phones
            containerWidth < 1300.dp -> WindowSizeClass.Medium // Large phones/Small tablets
            else -> WindowSizeClass.Expanded // Tablets/Desktop
        }
    }
}

enum class WindowSizeClass {
    Compact, // Phone
    Medium, // Tablet
    Expanded, // Desktop
}
