package com.nyinyi.dailychallenge.feature.play.shared.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.nyinyi.dailychallenge.feature.play.shared.components.QuizStateScreen

@Composable
fun ErrorPlayState(
    message: String,
    onRetry: () -> Unit,
) {
    QuizStateScreen(
        message = message,
        icon = Icons.Default.Error,
        iconTint = MaterialTheme.colorScheme.error,
        actionLabel = "Try Again",
        actionIcon = Icons.Default.Refresh,
        onAction = onRetry,
    )
}
