package com.nyinyi.dailychallenge.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object DailyChallengeSpacing {
    val xSmall: Dp = 4.dp
    val small: Dp = 8.dp
    val medium: Dp = 12.dp
    val large: Dp = 16.dp
    val xLarge: Dp = 24.dp
    val xxLarge: Dp = 32.dp

    val screenPadding: Dp = large
    val screenPaddingLarge: Dp = xLarge
    val sectionSpacing: Dp = xLarge
    val cardContentPadding: Dp = large
}

object DailyChallengeShapes {
    val small = RoundedCornerShape(8.dp)
    val medium = RoundedCornerShape(12.dp)
    val large = RoundedCornerShape(16.dp)
    val extraLarge = RoundedCornerShape(24.dp)
}

object DailyChallengeElevation {
    val low: Dp = 2.dp
    val medium: Dp = 4.dp
    val high: Dp = 8.dp
}

object DailyChallengeColors {
    val success: Color
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val warning: Color
        @Composable
        get() = MaterialTheme.colorScheme.tertiary

    val danger: Color
        @Composable
        get() = MaterialTheme.colorScheme.error

    val surfaceMuted: Color
        @Composable
        get() = MaterialTheme.colorScheme.surfaceVariant

    val surfaceAccent: Color
        @Composable
        get() = MaterialTheme.colorScheme.primaryContainer

    val topBar: Color
        @Composable
        get() = MaterialTheme.colorScheme.background
}
