package com.nyinyi.dailychallenge.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data class QuestionDetail(
        val questionId: String,
    ) : Routes

    @Serializable
    data object QuestionList : Routes

    @Serializable
    data object QuizScreen : Routes

    @Serializable
    data object MultipleChoiceScreen : Routes

    @Serializable
    data object MultipleSelectScreen : Routes
}
