package com.nyinyi.dailychallenge.data.remote

object ApiConstants {
    const val BASE_URL = "https://nyinyiz.github.io/daily_challenges_data/"

    // Endpoints
    const val DAILY_CHALLENGES = "daily_challenges.json"
    const val PROGRAMMING_TIPS = "programming_tips.json"

    fun getTrueFalseChallengesEndpoint(category: String): String = "true_or_false_challenges_${category.lowercase()}.json"

    fun getMultipleChoiceChallengesEndpoint(category: String): String = "multiple_choice_challenges_${category.lowercase()}.json"

    fun getMultipleSelectChallengesEndpoint(category: String): String = "multiple_select_challenges_${category.lowercase()}.json"

    fun getMatchingGameChallengesEndpoint(category: String): String = "matching_challenges_${category.lowercase()}.json"
}
