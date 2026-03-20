package com.nyinyi.dailychallenge.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nyinyi.dailychallenge.data.repository.ChallengeCatalogRepository
import com.nyinyi.dailychallenge.data.repository.ChallengeCatalogRepositoryImpl
import com.nyinyi.dailychallenge.data.repository.ChallengePlayRepository
import com.nyinyi.dailychallenge.data.repository.ChallengePlayRepositoryImpl
import com.nyinyi.dailychallenge.data.repository.SettingsRepository
import com.nyinyi.dailychallenge.data.repository.UserProfileRepository
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepositoryImpl
import org.koin.dsl.module

val repositoryModule =
    module {
        single { UserPreferencesRepositoryImpl(get()) }
        single<SettingsRepository> { get<UserPreferencesRepositoryImpl>() }
        single<UserProfileRepository> { get<UserPreferencesRepositoryImpl>() }

        single<ChallengeCatalogRepository> { ChallengeCatalogRepositoryImpl(get()) }
        single<ChallengePlayRepository> { ChallengePlayRepositoryImpl(get(), get()) }
    }

fun preferencesModule(dataStore: DataStore<Preferences>) =
    module {
        single { dataStore }
    }
