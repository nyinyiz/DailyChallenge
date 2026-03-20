package com.nyinyi.dailychallenge.feature.play.shared.components

import androidx.compose.runtime.Composable
import com.nyinyi.dailychallenge.feature.play.shared.components.QuizStateScreen

@Composable
fun LoadingPlayState() {
    QuizStateScreen(
        message = "Loading questions...",
        showLoadingIndicator = true,
    )
}
