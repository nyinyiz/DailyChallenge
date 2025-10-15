package com.nyinyi.dailychallenge.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nyinyi.dailychallenge.data.remote.ChallengesApiService
import com.nyinyi.dailychallenge.data.remote.logHttp
import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
import com.nyinyi.dailychallenge.data.repository.ChallengesRepositoryImpl
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepository
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepositoryImpl
import com.nyinyi.dailychallenge.ui.AppViewModel
import com.nyinyi.dailychallenge.ui.screens.list.QuestionListViewModel
import com.nyinyi.dailychallenge.ui.screens.play.PlayScreenContentViewModel
import com.nyinyi.dailychallenge.ui.screens.play.matching.MatchingGameViewModel
import com.nyinyi.dailychallenge.ui.screens.play.mcq.MultipleChoiceViewModel
import com.nyinyi.dailychallenge.ui.screens.play.multiselect.MultipleSelectViewModel
import com.nyinyi.dailychallenge.ui.screens.play.quiz.QuizScreenViewModel
import com.nyinyi.dailychallenge.ui.screens.profile.ProfileViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        single {
            HttpClient {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                    )
                }
                install(Logging) {
                    logger =
                        object : Logger {
                            override fun log(message: String) {
                                logHttp(message)
                            }
                        }
                    level = LogLevel.ALL
                }
            }
        }

        single { ChallengesApiService(get()) }

        single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }

        single<ChallengesRepository> {
            ChallengesRepositoryImpl(get(), get())
        }

        viewModel {
            AppViewModel(get(), get())
        }
        viewModel {
            QuestionListViewModel(get())
        }
        viewModel {
            QuizScreenViewModel(get(), get())
        }
        viewModel {
            MultipleChoiceViewModel(get(), get())
        }
        viewModel {
            MultipleSelectViewModel(get(), get())
        }
        viewModel {
            MatchingGameViewModel(get(), get())
        }
        viewModel {
            PlayScreenContentViewModel(get())
        }
        viewModel {
            ProfileViewModel(get())
        }
    }

fun preferencesModule(dataStore: DataStore<Preferences>) =
    module {
        single { dataStore }
    }
