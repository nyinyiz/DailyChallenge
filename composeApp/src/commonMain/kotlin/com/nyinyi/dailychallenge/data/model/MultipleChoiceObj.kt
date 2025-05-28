package com.nyinyi.dailychallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MultipleChoiceObj(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val difficulty: String,
    val explanation: String,
)

data class MultipleChoiceResult(
    val score: Int,
    val totalQuestions: Int,
    val incorrectAnswers: List<MultipleChoiceObj>,
)
