package com.nyinyi.dailychallenge.di

import com.nyinyi.dailychallenge.ui.AppViewModel
import com.nyinyi.dailychallenge.ui.screens.detail.QuestionDetailViewModel
import com.nyinyi.dailychallenge.ui.screens.list.QuestionListViewModel
import com.nyinyi.dailychallenge.ui.screens.play.PlayScreenContentViewModel
import com.nyinyi.dailychallenge.ui.screens.play.matching.MatchingGameViewModel
import com.nyinyi.dailychallenge.ui.screens.play.mcq.MultipleChoiceViewModel
import com.nyinyi.dailychallenge.ui.screens.play.multiselect.MultipleSelectViewModel
import com.nyinyi.dailychallenge.ui.screens.play.quiz.QuizScreenViewModel
import com.nyinyi.dailychallenge.ui.screens.profile.ProfileViewModel
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
