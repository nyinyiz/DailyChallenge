package com.nyinyi.dailychallenge.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.UserProfile
import com.nyinyi.dailychallenge.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            userProfileRepository.userProfile.collectLatest { userProfile ->
                val currentEditorState = (_uiState.value as? ProfileUiState.Success)?.editorState
                _uiState.value =
                    ProfileUiState.Success(
                        userProfile = userProfile,
                        editorState =
                            if (currentEditorState == null || !currentEditorState.isDirty) {
                                ProfileEditorState.from(userProfile)
                            } else {
                                currentEditorState
                            },
                    )
            }
        }
    }

    fun onEditorNameChanged(name: String) {
        updateEditor { it.copy(name = name) }
    }

    fun onEditorEmailChanged(email: String) {
        updateEditor { it.copy(email = email) }
    }

    fun resetEditor() {
        val currentState = _uiState.value as? ProfileUiState.Success ?: return
        _uiState.value = currentState.copy(editorState = ProfileEditorState.from(currentState.userProfile))
    }

    fun saveProfileEdits() {
        val currentState = _uiState.value as? ProfileUiState.Success ?: return
        viewModelScope.launch {
            userProfileRepository.updateUserName(currentState.editorState.name)
            userProfileRepository.updateUserEmail(currentState.editorState.email)
        }
    }

    fun updateDarkTheme(darkTheme: Boolean) {
        viewModelScope.launch {
            userProfileRepository.updateDarkTheme(darkTheme)
        }
    }

    fun clearFailedExercises() {
        viewModelScope.launch {
            userProfileRepository.clearFailedExercises()
        }
    }

    private fun updateEditor(transform: (ProfileEditorState) -> ProfileEditorState) {
        val currentState = _uiState.value as? ProfileUiState.Success ?: return
        _uiState.value = currentState.copy(editorState = transform(currentState.editorState))
    }
}

sealed class ProfileUiState {
    data object Loading : ProfileUiState()

    data class Success(
        val userProfile: UserProfile,
        val editorState: ProfileEditorState,
    ) : ProfileUiState()

    data class Error(
        val message: String,
    ) : ProfileUiState()
}

data class ProfileEditorState(
    val name: String,
    val email: String,
    val originalName: String,
    val originalEmail: String,
) {
    val isDirty: Boolean = name != originalName || email != originalEmail

    companion object {
        fun from(userProfile: UserProfile) =
            ProfileEditorState(
                name = userProfile.name,
                email = userProfile.email,
                originalName = userProfile.name,
                originalEmail = userProfile.email,
            )
    }
}
