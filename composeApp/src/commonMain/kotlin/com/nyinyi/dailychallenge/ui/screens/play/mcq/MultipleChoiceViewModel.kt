package com.nyinyi.dailychallenge.ui.screens.play.mcq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.repository.ChallengePlayRepository
import com.nyinyi.dailychallenge.data.repository.UserProfileRepository
import com.nyinyi.dailychallenge.feature.play.mcq.MultipleChoiceSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MultipleChoiceUiState {
    data object Loading : MultipleChoiceUiState()

    data class Error(
        val message: String,
    ) : MultipleChoiceUiState()

    data object Empty : MultipleChoiceUiState()

    data class Quiz(
        val session: MultipleChoiceSession,
    ) : MultipleChoiceUiState()
}

class MultipleChoiceViewModel(
    private val repository: ChallengePlayRepository,
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MultipleChoiceUiState>(MultipleChoiceUiState.Loading)
    val uiState: StateFlow<MultipleChoiceUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            try {
                val questions = repository.getMultipleChoiceChallenges()
                if (questions.isEmpty()) {
                    _uiState.value = MultipleChoiceUiState.Empty
                } else {
                    _uiState.value =
                        MultipleChoiceUiState.Quiz(
                            session = MultipleChoiceSession(questions = questions),
                        )
                }
            } catch (e: Exception) {
                _uiState.value = MultipleChoiceUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun answerQuestion(selectedAnswer: String) {
        val currentState = _uiState.value as? MultipleChoiceUiState.Quiz ?: return
        val outcome = currentState.session.answer(selectedAnswer)
        outcome.failedQuestion?.let { failedQuestion ->
            viewModelScope.launch {
                userProfileRepository.addFailedMultipleChoiceQuestion(failedQuestion)
            }
        }
        _uiState.value = currentState.copy(session = outcome.nextSession)
    }

    fun restartQuiz() {
        val currentState = _uiState.value as? MultipleChoiceUiState.Quiz ?: return
        _uiState.value = currentState.copy(session = currentState.session.restart())
    }
}
