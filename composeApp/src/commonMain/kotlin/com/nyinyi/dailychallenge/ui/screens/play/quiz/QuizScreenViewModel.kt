package com.nyinyi.dailychallenge.ui.screens.play.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.repository.ChallengePlayRepository
import com.nyinyi.dailychallenge.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizScreenViewModel(
    private val repository: ChallengePlayRepository,
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<QuizState>(QuizState.Loading)
    val state: StateFlow<QuizState> = _state.asStateFlow()

    init {
        getTrueFalseChallenges()
    }

    fun getTrueFalseChallenges() {
        viewModelScope.launch {
            _state.value = QuizState.Loading
            try {
                val challenges = repository.getTrueFalseChallenges()
                _state.value =
                    if (challenges.isEmpty()) {
                        QuizState.Empty
                    } else {
                        QuizState.Success(TrueFalseQuizSession(challenges))
                    }
            } catch (e: Exception) {
                _state.value = QuizState.Error(e.message ?: "Error loading questions")
            }
        }
    }

    fun answerQuestion(answer: Boolean) {
        val currentState = _state.value as? QuizState.Success ?: return
        val outcome = currentState.session.answer(answer)

        _state.value = currentState.copy(session = outcome.nextSession)

        outcome.failedQuestion?.let { failedQuestion ->
            viewModelScope.launch {
                userProfileRepository.addFailedQuizCard(failedQuestion)
            }
        }
    }

    fun restartQuiz() {
        val currentState = _state.value as? QuizState.Success ?: return
        _state.value = currentState.copy(session = currentState.session.restart())
    }
}

sealed class QuizState {
    object Loading : QuizState()

    data class Success(
        val session: TrueFalseQuizSession,
    ) : QuizState()

    data class Error(
        val message: String,
    ) : QuizState()

    object Empty : QuizState()
}
