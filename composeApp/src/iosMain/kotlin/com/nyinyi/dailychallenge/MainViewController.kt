package com.nyinyi.dailychallenge

import androidx.compose.ui.window.ComposeUIViewController
import com.nyinyi.dailychallenge.di.KoinInitializer
import com.nyinyi.dailychallenge.ui.App

fun MainViewController() = ComposeUIViewController {
    val dataStore = createDataStore()
    KoinInitializer.init(dataStore)
    App()
}
