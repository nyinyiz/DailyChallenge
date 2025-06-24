package com.nyinyi.dailychallenge.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.nyinyi.dailychallenge.data.model.Category
import com.nyinyi.dailychallenge.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesRepository {
    private object PreferencesKeys {
        val SELECTED_CATEGORY = stringPreferencesKey("selected_category")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val FAILED_QUIZ_CARDS = stringSetPreferencesKey("failed_quiz_cards")
        val FAILED_MULTIPLE_CHOICE = stringSetPreferencesKey("failed_multiple_choice")
        val FAILED_MULTIPLE_SELECT = stringSetPreferencesKey("failed_multiple_select")
    }

    override val selectedCategory: Flow<Category> =
        dataStore.data.map { preferences ->
            try {
                val categoryName =
                    preferences[PreferencesKeys.SELECTED_CATEGORY] ?: Category.ANDROID.name
                Category.valueOf(categoryName)
            } catch (e: Exception) {
                Category.ANDROID
            }
        }

    override val userProfile: Flow<UserProfile> =
        dataStore.data.map { preferences ->
            val name = preferences[PreferencesKeys.USER_NAME] ?: ""
            val email = preferences[PreferencesKeys.USER_EMAIL] ?: ""
            val failedQuizCards =
                preferences[PreferencesKeys.FAILED_QUIZ_CARDS]?.toList() ?: emptyList()
            val failedMultipleChoice =
                preferences[PreferencesKeys.FAILED_MULTIPLE_CHOICE]?.toList() ?: emptyList()
            val failedMultipleSelect =
                preferences[PreferencesKeys.FAILED_MULTIPLE_SELECT]?.toList() ?: emptyList()

            UserProfile(
                name = name,
                email = email,
                failedQuizCards = failedQuizCards,
                failedMultipleChoiceQuestions = failedMultipleChoice,
                failedMultipleSelectQuestions = failedMultipleSelect,
            )
        }

    override suspend fun setSelectedCategory(category: Category) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CATEGORY] = category.name
        }
    }

    override suspend fun setUserProfile(userProfile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = userProfile.name
            preferences[PreferencesKeys.USER_EMAIL] = userProfile.email
            preferences[PreferencesKeys.FAILED_QUIZ_CARDS] = userProfile.failedQuizCards.toSet()
            preferences[PreferencesKeys.FAILED_MULTIPLE_CHOICE] =
                userProfile.failedMultipleChoiceQuestions.toSet()
            preferences[PreferencesKeys.FAILED_MULTIPLE_SELECT] =
                userProfile.failedMultipleSelectQuestions.toSet()
        }
    }

    override suspend fun updateUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    override suspend fun updateUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_EMAIL] = email
        }
    }

    override suspend fun addFailedQuizCard(questionId: String) {
        dataStore.edit { preferences ->
            val currentFailed =
                preferences[PreferencesKeys.FAILED_QUIZ_CARDS]?.toMutableSet() ?: mutableSetOf()
            currentFailed.add(questionId)
            preferences[PreferencesKeys.FAILED_QUIZ_CARDS] = currentFailed
        }
    }

    override suspend fun addFailedMultipleChoiceQuestion(questionId: String) {
        dataStore.edit { preferences ->
            val currentFailed =
                preferences[PreferencesKeys.FAILED_MULTIPLE_CHOICE]?.toMutableSet()
                    ?: mutableSetOf()
            currentFailed.add(questionId)
            preferences[PreferencesKeys.FAILED_MULTIPLE_CHOICE] = currentFailed
        }
    }

    override suspend fun addFailedMultipleSelectQuestion(questionId: String) {
        dataStore.edit { preferences ->
            val currentFailed =
                preferences[PreferencesKeys.FAILED_MULTIPLE_SELECT]?.toMutableSet()
                    ?: mutableSetOf()
            currentFailed.add(questionId)
            preferences[PreferencesKeys.FAILED_MULTIPLE_SELECT] = currentFailed
        }
    }

    override suspend fun clearFailedExercises() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.FAILED_QUIZ_CARDS)
            preferences.remove(PreferencesKeys.FAILED_MULTIPLE_CHOICE)
            preferences.remove(PreferencesKeys.FAILED_MULTIPLE_SELECT)
        }
    }
}
