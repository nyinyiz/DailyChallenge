package com.nyinyi.dailychallenge.ui.screens.play.quiz.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import com.nyinyi.dailychallenge.ui.screens.play.components.QuizStateScreen

@Composable
fun EmptyState(onRetry: () -> Unit) {
    QuizStateScreen(
        title = "No Questions Available",
        message = "There are no quiz questions available at the moment. Please try again later.",
        icon = Icons.Default.QuestionMark,
        actionLabel = "Refresh",
        actionIcon = Icons.Default.Refresh,
        onAction = onRetry,
    )
}
