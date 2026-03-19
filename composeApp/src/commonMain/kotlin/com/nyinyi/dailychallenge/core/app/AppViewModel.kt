package com.nyinyi.dailychallenge.core.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AppViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val userProfile = userPreferencesRepository.userProfile

    fun toggleTheme() {
        viewModelScope.launch {
            val currentProfile = userProfile.firstOrNull()
            if (currentProfile != null) {
                userPreferencesRepository.updateDarkTheme(!currentProfile.darkTheme)
            }
        }
    }
}
