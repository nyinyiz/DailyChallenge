package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.DailyChallengeObj

interface ChallengeCatalogRepository {
    suspend fun getDailyChallengeById(id: String): DailyChallengeObj

    suspend fun getDailyChallenges(): List<DailyChallengeObj>

    suspend fun getRandomChallenge(): DailyChallengeObj
}
