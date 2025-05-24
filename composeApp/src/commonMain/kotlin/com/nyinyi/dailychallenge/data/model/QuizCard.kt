package com.nyinyi.dailychallenge.data.model

import dailychallenge.composeapp.generated.resources.Res
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class QuizCard(
    val id: Int,
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

suspend fun loadTrueFalseQuizQuestions(): List<QuizCard> {
    try {
        val readBytes = Res.readBytes("files/true_or_false_challenges.json")
        val jsonString = readBytes.decodeToString()
        return Json.decodeFromString(jsonString)
    } catch (e: Exception) {
        println(e.message)
        return emptyList()
    }
}
