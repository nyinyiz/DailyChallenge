package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.Category
import com.nyinyi.dailychallenge.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val selectedCategory: Flow<Category>
    val userProfile: Flow<UserProfile>

    suspend fun setSelectedCategory(category: Category)

    suspend fun setUserProfile(userProfile: UserProfile)

    suspend fun updateUserName(name: String)

    suspend fun updateUserEmail(email: String)

    suspend fun addFailedQuizCard(questionId: String)

    suspend fun addFailedMultipleChoiceQuestion(questionId: String)

    suspend fun addFailedMultipleSelectQuestion(questionId: String)

    suspend fun clearFailedExercises()
}
