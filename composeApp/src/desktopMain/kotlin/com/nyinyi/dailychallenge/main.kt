package com.nyinyi.dailychallenge

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nyinyi.dailychallenge.di.KoinInitializer
import com.nyinyi.dailychallenge.ui.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.File

fun createPreferencesDataStore(producePath: () -> File): DataStore<Preferences> =
    androidx.datastore.preferences.core.PreferenceDataStoreFactory.create(
        corruptionHandler = null,
        migrations = emptyList(),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = producePath,
    )

fun main() {
    val dataStore: DataStore<Preferences> =
        createPreferencesDataStore {
            val userHomeDir = System.getProperty("user.home")
            val parentDirName =
                ".${DATA_STORE_FILE_NAME.substringBeforeLast(".")}"
            File(userHomeDir, parentDirName).resolve(DATA_STORE_FILE_NAME)
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
