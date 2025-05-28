package com.nyinyi.dailychallenge.di

import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
import com.nyinyi.dailychallenge.data.repository.ChallengesRepositoryImpl
import com.nyinyi.dailychallenge.ui.AppViewModel
import com.nyinyi.dailychallenge.ui.screens.list.QuestionListViewModel
import com.nyinyi.dailychallenge.ui.screens.play.mcq.MultipleChoiceViewModel
import com.nyinyi.dailychallenge.ui.screens.play.quiz.QuizScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        single<ChallengesRepository> {
            ChallengesRepositoryImpl()
        }
        viewModel {
            AppViewModel(get())
        }
        viewModel {
            QuestionListViewModel(get())
        }
        viewModel {
            QuizScreenViewModel(get())
        }
        viewModel {
            MultipleChoiceViewModel(get())
        }
    }
