package com.nyinyi.dailychallenge.core.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AppViewModel(
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {
    val userProfile = userProfileRepository.userProfile

    fun toggleTheme() {
        viewModelScope.launch {
            val currentProfile = userProfile.firstOrNull()
            if (currentProfile != null) {
                userProfileRepository.updateDarkTheme(!currentProfile.darkTheme)
            }
        }
    }
}
