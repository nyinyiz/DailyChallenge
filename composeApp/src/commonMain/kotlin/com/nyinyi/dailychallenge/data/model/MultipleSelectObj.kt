package com.nyinyi.dailychallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MultipleSelectObj(
    val question: String,
    val options: List<String>,
    val correctAnswers: List<String>,
    val difficulty: String,
    val explanation: String,
)

data class MultipleSelectResult(
    val score: Int,
    val totalQuestions: Int,
    val incorrectAnswers: List<MultipleSelectObj>,
)
