package com.nyinyi.dailychallenge.data.remote

object ApiConstants {
    const val BASE_URL = "https://nyinyiz.github.io/daily_challenges_data/"

    // Endpoints
    const val DAILY_CHALLENGES = "content/daily/daily_challenges.json"
    const val PROGRAMMING_TIPS = "content/tips/programming_tips.json"

    fun getTrueFalseChallengesEndpoint(category: String): String =
        "content/quizzes/${category.lowercase()}/true_or_false.json"

    fun getMultipleChoiceChallengesEndpoint(category: String): String =
        "content/quizzes/${category.lowercase()}/multiple_choice.json"

    fun getMultipleSelectChallengesEndpoint(category: String): String =
        "content/quizzes/${category.lowercase()}/multiple_select.json"

    fun getMatchingGameChallengesEndpoint(category: String): String =
        "content/quizzes/${category.lowercase()}/matching.json"
}
