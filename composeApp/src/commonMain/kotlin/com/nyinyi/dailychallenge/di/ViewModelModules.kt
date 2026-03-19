package com.nyinyi.dailychallenge.di

import com.nyinyi.dailychallenge.core.app.AppViewModel
import com.nyinyi.dailychallenge.feature.challenge.detail.QuestionDetailViewModel
import com.nyinyi.dailychallenge.feature.challenge.list.QuestionListViewModel
import com.nyinyi.dailychallenge.feature.profile.ProfileViewModel
import com.nyinyi.dailychallenge.ui.screens.play.PlayScreenContentViewModel
import com.nyinyi.dailychallenge.ui.screens.play.matching.MatchingGameViewModel
import com.nyinyi.dailychallenge.ui.screens.play.mcq.MultipleChoiceViewModel
import com.nyinyi.dailychallenge.ui.screens.play.multiselect.MultipleSelectViewModel
import com.nyinyi.dailychallenge.ui.screens.play.quiz.QuizScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appViewModelModule =
    module {
        viewModel {
            AppViewModel(get())
        }
    }

val featureViewModelModule =
    module {
        viewModel {
            QuestionListViewModel(get())
        }
        viewModel {
            QuestionDetailViewModel(get())
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
