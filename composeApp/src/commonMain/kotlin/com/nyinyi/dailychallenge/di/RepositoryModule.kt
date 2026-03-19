package com.nyinyi.dailychallenge.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
import com.nyinyi.dailychallenge.data.repository.ChallengesRepositoryImpl
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepository
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepositoryImpl
import org.koin.dsl.module

val repositoryModule =
    module {
        single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }

        single<ChallengesRepository> {
            ChallengesRepositoryImpl(get(), get())
        }
    }

fun preferencesModule(dataStore: DataStore<Preferences>) =
    module {
        single { dataStore }
    }
