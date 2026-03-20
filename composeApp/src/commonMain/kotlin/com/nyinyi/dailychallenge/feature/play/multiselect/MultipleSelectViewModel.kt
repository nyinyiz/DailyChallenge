package com.nyinyi.dailychallenge.feature.play.multiselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.repository.ChallengePlayRepository
import com.nyinyi.dailychallenge.data.repository.UserProfileRepository
import com.nyinyi.dailychallenge.feature.play.multiselect.MultipleSelectSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MultipleSelectUiState {
    data object Loading : MultipleSelectUiState()

    data class Error(
        val message: String,
    ) : MultipleSelectUiState()

    data object Empty : MultipleSelectUiState()

    data class Quiz(
        val session: MultipleSelectSession,
    ) : MultipleSelectUiState()
}

class MultipleSelectViewModel(
    private val repository: ChallengePlayRepository,
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MultipleSelectUiState>(MultipleSelectUiState.Loading)
    val uiState: StateFlow<MultipleSelectUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            try {
                val questions = repository.getMultipleSelectChallenges()
                if (questions.isEmpty()) {
                    _uiState.value = MultipleSelectUiState.Empty
                } else {
                    _uiState.value =
                        MultipleSelectUiState.Quiz(
                            session = MultipleSelectSession(questions = questions),
                        )
                }
            } catch (e: Exception) {
                _uiState.value = MultipleSelectUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun toggleOptionSelection(option: String) {
        val currentState = _uiState.value as? MultipleSelectUiState.Quiz ?: return
        _uiState.value = currentState.copy(session = currentState.session.toggleOption(option))
    }

    fun submitAnswer() {
        val currentState = _uiState.value as? MultipleSelectUiState.Quiz ?: return
        val outcome = currentState.session.submitAnswer()
        outcome.failedQuestion?.let { failedQuestion ->
            viewModelScope.launch {
                userProfileRepository.addFailedMultipleSelectQuestion(failedQuestion)
            }
        }
        _uiState.value = currentState.copy(session = outcome.nextSession)
    }

    fun restartQuiz() {
        val currentState = _uiState.value as? MultipleSelectUiState.Quiz ?: return
        _uiState.value = currentState.copy(session = currentState.session.restart())
    }
}
