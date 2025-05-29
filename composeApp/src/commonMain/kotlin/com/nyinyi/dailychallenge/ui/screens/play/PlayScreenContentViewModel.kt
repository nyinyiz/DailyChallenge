package com.nyinyi.dailychallenge.ui.screens.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.Category
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayScreenContentViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val selectedCategory =
        userPreferencesRepository.selectedCategory
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Category.ANDROID,
            )

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            userPreferencesRepository.setSelectedCategory(category)
        }
    }
}
