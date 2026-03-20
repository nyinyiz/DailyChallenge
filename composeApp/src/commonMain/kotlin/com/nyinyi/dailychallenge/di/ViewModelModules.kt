package com.nyinyi.dailychallenge.di

import com.nyinyi.dailychallenge.core.app.AppViewModel
import com.nyinyi.dailychallenge.feature.challenge.detail.QuestionDetailViewModel
import com.nyinyi.dailychallenge.feature.challenge.list.QuestionListViewModel
import com.nyinyi.dailychallenge.feature.profile.ProfileViewModel
import com.nyinyi.dailychallenge.feature.play.home.PlayHubViewModel
import com.nyinyi.dailychallenge.feature.play.matching.MatchingGameViewModel
import com.nyinyi.dailychallenge.feature.play.mcq.MultipleChoiceViewModel
import com.nyinyi.dailychallenge.feature.play.multiselect.MultipleSelectViewModel
import com.nyinyi.dailychallenge.feature.play.truefalse.TrueFalseViewModel
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
            TrueFalseViewModel(get(), get())
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
            PlayHubViewModel(get())
        }
        viewModel {
            ProfileViewModel(get())
        }
    }
