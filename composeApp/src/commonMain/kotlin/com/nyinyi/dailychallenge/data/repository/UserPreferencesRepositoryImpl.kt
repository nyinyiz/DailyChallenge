package com.nyinyi.dailychallenge.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nyinyi.dailychallenge.data.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {
    private object PreferencesKeys {
        val SELECTED_CATEGORY = stringPreferencesKey("selected_category")
    }

    override val selectedCategory: Flow<Category> = dataStore.data.map { preferences ->
        try {
            val categoryName =
                preferences[PreferencesKeys.SELECTED_CATEGORY] ?: Category.ANDROID.name
            Category.valueOf(categoryName)
        } catch (e: Exception) {
            Category.ANDROID
        }
    }

    override suspend fun setSelectedCategory(category: Category) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CATEGORY] = category.name
        }
    }
}