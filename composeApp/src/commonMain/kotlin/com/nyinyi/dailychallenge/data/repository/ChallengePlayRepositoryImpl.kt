package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.MatchingGameObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.remote.ChallengesApiService
import com.nyinyi.dailychallenge.data.remote.getOrDefault
import kotlinx.coroutines.flow.first

class ChallengePlayRepositoryImpl(
    private val settingsRepository: SettingsRepository,
    private val apiService: ChallengesApiService,
) : ChallengePlayRepository {
    override suspend fun getTrueFalseChallenges(): List<QuizCard> {
        val category = settingsRepository.selectedCategory.first()
        return apiService
            .getTrueFalseChallenges(category.name)
            .getOrDefault(emptyList())
            .shuffled()
            .take(10)
    }

    override suspend fun getMultipleChoiceChallenges(): List<MultipleChoiceObj> {
        val category = settingsRepository.selectedCategory.first()
        return apiService
            .getMultipleChoiceChallenges(category.name)
            .getOrDefault(emptyList())
            .shuffled()
            .take(10)
    }

    override suspend fun getMultipleSelectChallenges(): List<MultipleSelectObj> {
        val category = settingsRepository.selectedCategory.first()
        return apiService
            .getMultipleSelectChallenges(category.name)
            .getOrDefault(emptyList())
            .shuffled()
            .take(10)
    }

    override suspend fun getMatchingGameChallenges(): List<MatchingGameObj> {
        val category = settingsRepository.selectedCategory.first()
        return apiService
            .getMatchingGameChallenges(category.name)
            .getOrDefault(emptyList())
            .shuffled()
            .take(3)
    }
}
