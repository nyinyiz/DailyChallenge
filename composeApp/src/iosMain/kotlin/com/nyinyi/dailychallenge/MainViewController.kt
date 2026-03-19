package com.nyinyi.dailychallenge

import androidx.compose.ui.window.ComposeUIViewController
import com.nyinyi.dailychallenge.core.app.App
import com.nyinyi.dailychallenge.di.KoinInitializer

fun MainViewController() =
    ComposeUIViewController {
        val dataStore = createDataStore()
        KoinInitializer.init(dataStore)
        App()
    }
