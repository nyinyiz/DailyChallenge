package com.nyinyi.dailychallenge.feature.play.truefalse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.repository.ChallengePlayRepository
import com.nyinyi.dailychallenge.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrueFalseViewModel(
    private val repository: ChallengePlayRepository,
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<TrueFalseUiState>(TrueFalseUiState.Loading)
    val state: StateFlow<TrueFalseUiState> = _state.asStateFlow()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            _state.value = TrueFalseUiState.Loading
            try {
                val challenges = repository.getTrueFalseChallenges()
                _state.value =
                    if (challenges.isEmpty()) {
                        TrueFalseUiState.Empty
                    } else {
                        TrueFalseUiState.Success(TrueFalseSession(challenges))
                    }
            } catch (e: Exception) {
                _state.value = TrueFalseUiState.Error(e.message ?: "Error loading questions")
            }
        }
    }

    fun answerQuestion(answer: Boolean) {
        val currentState = _state.value as? TrueFalseUiState.Success ?: return
        val outcome = currentState.session.answer(answer)

        _state.value = currentState.copy(session = outcome.nextSession)

        outcome.failedQuestion?.let { failedQuestion ->
            viewModelScope.launch {
                userProfileRepository.addFailedQuizCard(failedQuestion)
            }
        }
    }

    fun restartQuiz() {
        val currentState = _state.value as? TrueFalseUiState.Success ?: return
        _state.value = currentState.copy(session = currentState.session.restart())
    }
}

sealed class TrueFalseUiState {
    object Loading : TrueFalseUiState()

    data class Success(
        val session: TrueFalseSession,
    ) : TrueFalseUiState()

    data class Error(
        val message: String,
    ) : TrueFalseUiState()

    object Empty : TrueFalseUiState()
}
