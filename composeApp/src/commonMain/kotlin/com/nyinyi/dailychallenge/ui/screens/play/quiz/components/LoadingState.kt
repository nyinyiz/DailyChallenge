package com.nyinyi.dailychallenge.ui.screens.play.quiz.components

import androidx.compose.runtime.Composable
import com.nyinyi.dailychallenge.ui.screens.play.components.QuizStateScreen

@Composable
fun LoadingState() {
    QuizStateScreen(
        message = "Loading questions...",
        showLoadingIndicator = true,
    )
}
