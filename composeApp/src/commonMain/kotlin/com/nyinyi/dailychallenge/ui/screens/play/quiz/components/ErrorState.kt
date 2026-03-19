package com.nyinyi.dailychallenge.ui.screens.play.quiz.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.nyinyi.dailychallenge.ui.screens.play.components.QuizStateScreen

@Composable
fun ErrorState(
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
