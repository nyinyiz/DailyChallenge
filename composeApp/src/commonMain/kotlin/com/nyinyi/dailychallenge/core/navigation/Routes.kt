package com.nyinyi.dailychallenge.core.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object Home : Routes

    @Serializable
    data object ChallengeList : Routes

    @Serializable
    data object Profile : Routes

    @Serializable
    data class QuestionDetail(
        val questionId: String,
    ) : Routes

    @Serializable
    data object QuizScreen : Routes

    @Serializable
    data object MultipleChoiceScreen : Routes

    @Serializable
    data object MultipleSelectScreen : Routes

    @Serializable
    data object MatchingGameScreen : Routes
}
