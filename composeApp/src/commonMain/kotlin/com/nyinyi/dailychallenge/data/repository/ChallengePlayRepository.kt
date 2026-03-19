package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.MatchingGameObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.QuizCard

interface ChallengePlayRepository {
    suspend fun getTrueFalseChallenges(): List<QuizCard>

    suspend fun getMultipleChoiceChallenges(): List<MultipleChoiceObj>

    suspend fun getMultipleSelectChallenges(): List<MultipleSelectObj>

    suspend fun getMatchingGameChallenges(): List<MatchingGameObj>
}
