package com.nyinyi.dailychallenge

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.nyinyi.dailychallenge.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "DailyChallenge",
    ) {
        App()
    }
}