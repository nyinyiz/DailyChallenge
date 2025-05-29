package com.nyinyi.dailychallenge.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.context.startKoin

object KoinInitializer {
    fun init(dataStore: DataStore<Preferences>) {
        startKoin {
            modules(
                appModule,
                preferencesModule(dataStore)
            )
        }
    }
}
