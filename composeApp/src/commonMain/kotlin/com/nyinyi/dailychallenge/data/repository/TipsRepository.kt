package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.ProgrammingTip
import com.nyinyi.dailychallenge.data.remote.ChallengesApiService
import com.nyinyi.dailychallenge.data.remote.NetworkResult

class TipsRepository(
    private val apiService: ChallengesApiService,
) {
    suspend fun getAllTips(): List<ProgrammingTip> =
        when (val result = apiService.getProgrammingTips()) {
            is NetworkResult.Success -> result.data
            is NetworkResult.NetworkError,
            is NetworkResult.Error,
            -> {
                listOf(
                    ProgrammingTip(
                        id = "fallback",
                        category = "Programming Knowledge",
                        tip = "Write clean, maintainable code. Future you will thank present you!",
                    ),
                )
            }
        }

    suspend fun getRandomTip(): ProgrammingTip = getAllTips().randomOrNull() ?: getDefaultTip()

    private fun getDefaultTip(): ProgrammingTip =
        ProgrammingTip(
            id = "default",
            category = "Best Practices",
            tip = "Keep learning and coding every day!",
        )
}
