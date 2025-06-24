package com.nyinyi.dailychallenge.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.UserProfile
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            userPreferencesRepository.userProfile.collectLatest { userProfile ->
                _uiState.value = ProfileUiState.Success(userProfile)
            }
        }
    }

    fun updateUserName(name: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserName(name)
        }
    }

    fun updateUserEmail(email: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserEmail(email)
        }
    }

    fun clearFailedExercises() {
        viewModelScope.launch {
            userPreferencesRepository.clearFailedExercises()
        }
    }
}

sealed class ProfileUiState {
    data object Loading : ProfileUiState()

    data class Success(
        val userProfile: UserProfile,
    ) : ProfileUiState()

    data class Error(
        val message: String,
    ) : ProfileUiState()
}
