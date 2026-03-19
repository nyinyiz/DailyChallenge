package com.nyinyi.dailychallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nyinyi.dailychallenge.core.app.App
import com.nyinyi.dailychallenge.di.KoinInitializer

class MainActivity : ComponentActivity() {
    val dataStore by lazy { createDataStore(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        KoinInitializer.init(dataStore)

        setContent {
            App()
        }
    }
}
