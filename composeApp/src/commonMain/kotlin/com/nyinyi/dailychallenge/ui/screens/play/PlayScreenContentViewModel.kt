package com.nyinyi.dailychallenge.ui.screens.play

import androidx.datastore.core.IOException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.Category
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayScreenContentViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayScreenUiState())
    val uiState: StateFlow<PlayScreenUiState> = _uiState.asStateFlow()

    val selectedCategory =
        userPreferencesRepository.selectedCategory
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Category.ANDROID,
            )

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingCategoryUpdate = true,
                    categoryUpdateError = null,
                    categoryUpdateSuccessMessage = null,
                )
            }

            try {
                userPreferencesRepository.setSelectedCategory(category)
                _uiState.update {
                    it.copy(
                        isLoadingCategoryUpdate = false,
                        categoryUpdateSuccessMessage = "Category updated to ${category.name} successfully!",
                    )
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        isLoadingCategoryUpdate = false,
                        categoryUpdateError = "Network error. Failed to update category. Please try again. ${e.message}",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingCategoryUpdate = false,
                        categoryUpdateError = "An unexpected error occurred while updating category.",
                    )
                }
            }
        }
    }

    fun consumeCategoryUpdateError() {
        _uiState.update { it.copy(categoryUpdateError = null) }
    }

    fun consumeCategoryUpdateSuccessMessage() {
        _uiState.update { it.copy(categoryUpdateSuccessMessage = null) }
    }
}

data class PlayScreenUiState(
    val isLoadingCategoryUpdate: Boolean = false,
    val categoryUpdateError: String? = null,
    val categoryUpdateSuccessMessage: String? = null,
)
