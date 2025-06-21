package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.QuizCard

interface ChallengesRepository {
    suspend fun getDailyChallengeById(id: String): DailyChallengeObj

    suspend fun getDailyChallenges(): List<DailyChallengeObj>

    suspend fun getRandomChallenges(): DailyChallengeObj

    suspend fun getTrueFalseChallenges(): List<QuizCard>

    suspend fun getMultipleChoiceChallenges(): List<MultipleChoiceObj>

    suspend fun getMultipleSelectChallenges(): List<MultipleSelectObj>
}
