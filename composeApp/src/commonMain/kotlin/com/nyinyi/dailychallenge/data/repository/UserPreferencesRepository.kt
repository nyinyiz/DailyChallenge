package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.Category
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val selectedCategory: Flow<Category>

    suspend fun setSelectedCategory(category: Category)
}
