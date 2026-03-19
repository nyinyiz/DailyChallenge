package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.Category
import com.nyinyi.dailychallenge.data.model.MatchingGameObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val selectedCategory: Flow<Category>
    val userProfile: Flow<UserProfile>

    suspend fun setSelectedCategory(category: Category)

    suspend fun setUserProfile(userProfile: UserProfile)

    suspend fun updateUserName(name: String)

    suspend fun updateUserEmail(email: String)

    suspend fun updateDarkTheme(darkTheme: Boolean)

    suspend fun addFailedQuizCard(question: QuizCard)

    suspend fun addFailedMultipleChoiceQuestion(question: MultipleChoiceObj)

    suspend fun addFailedMultipleSelectQuestion(question: MultipleSelectObj)

    suspend fun addFailedMatchingGameQuestion(question: MatchingGameObj)

    suspend fun clearFailedExercises()
}
