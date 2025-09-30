package com.nyinyi.dailychallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MatchingGameObj(
    val question: String,
    val pairs: List<MatchingPair>,
    val difficulty: String,
    val explanation: String,
)

@Serializable
data class MatchingPair(
    val left: String,
    val right: String,
)

data class QuestionAttempt(
    val question: MatchingGameObj,
    val incorrectAttempts: Int,
    val isCompleted: Boolean,
)

data class MatchingGameResult(
    val score: Int,
    val totalQuestions: Int,
    val allQuestionAttempts: List<QuestionAttempt>,
)
