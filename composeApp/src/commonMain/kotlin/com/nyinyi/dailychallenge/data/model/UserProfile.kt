package com.nyinyi.dailychallenge.data.model

import kotlinx.serialization.Serializable

/**
 * Represents a user profile with personal information and failed exercises.
 */
@Serializable
data class UserProfile(
    val name: String = "",
    val email: String = "",
    val darkTheme: Boolean = true,
    val failedQuizCards: List<FailedQuestionRecord> = emptyList(),
    val failedMultipleChoiceQuestions: List<FailedQuestionRecord> = emptyList(),
    val failedMultipleSelectQuestions: List<FailedQuestionRecord> = emptyList(),
    val failedMatchingGameQuestions: List<FailedQuestionRecord> = emptyList(),
)
