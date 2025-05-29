package com.nyinyi.dailychallenge

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.nyinyi.dailychallenge.di.KoinInitializer
import com.nyinyi.dailychallenge.ui.App

fun main() {
    val dataStore =
        createDataStore {
            DATA_STORE_FILE_NAME
        }
    KoinInitializer.init(dataStore)

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "DailyChallenge",
        ) {
            App()
        }
    }
}
