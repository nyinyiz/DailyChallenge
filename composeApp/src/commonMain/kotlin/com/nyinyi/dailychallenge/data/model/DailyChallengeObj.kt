package com.nyinyi.dailychallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DailyChallengeObj(
    val id: String,
    val difficulty: String,
    // Easy, Medium, Hard
    val question: String,
    val questionCode: String = "",
    val answerCode: String = "",
)
