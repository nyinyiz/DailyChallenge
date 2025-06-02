package com.nyinyi.dailychallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizCard(
    val difficulty: String,
    val question: String,
    val correctAnswer: Boolean,
    val explanation: String,
)

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val incorrectAnswers: List<QuizCard>,
)
