package com.nyinyi.dailychallenge.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
import com.nyinyi.dailychallenge.data.repository.ChallengesRepositoryImpl
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepository
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepositoryImpl
import com.nyinyi.dailychallenge.ui.AppViewModel
import com.nyinyi.dailychallenge.ui.screens.list.QuestionListViewModel
import com.nyinyi.dailychallenge.ui.screens.play.PlayScreenContentViewModel
import com.nyinyi.dailychallenge.ui.screens.play.mcq.MultipleChoiceViewModel
import com.nyinyi.dailychallenge.ui.screens.play.multiselect.MultipleSelectViewModel
import com.nyinyi.dailychallenge.ui.screens.play.quiz.QuizScreenViewModel
import com.nyinyi.dailychallenge.ui.screens.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }

        single<ChallengesRepository> {
            ChallengesRepositoryImpl(get())
        }

        viewModel {
            AppViewModel(get())
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
