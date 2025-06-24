package com.nyinyi.dailychallenge.ui.screens.play.mcq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceResult
import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepository
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
        val questions: List<MultipleChoiceObj>,
        val currentQuestionIndex: Int = 0,
        val score: Int = 0,
        val answeredQuestions: Map<Int, String> = emptyMap(),
    ) : MultipleChoiceUiState() {
        val totalQuestions: Int = questions.size
        val currentQuestion: MultipleChoiceObj? = questions.getOrNull(currentQuestionIndex)
        val isComplete: Boolean = currentQuestionIndex >= totalQuestions
        val progressPercentage: Float =
            (currentQuestionIndex.toFloat() / totalQuestions.toFloat()) * 100

        fun toResult(): MultipleChoiceResult? {
            if (!isComplete) return null
            return MultipleChoiceResult(
                score = score,
                totalQuestions = totalQuestions,
                incorrectAnswers =
                    questions.filterIndexed { index, question ->
                        answeredQuestions[index] != question.correctAnswer
                    },
            )
        }
    }
}

class MultipleChoiceViewModel(
    private val repository: ChallengesRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
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
                            questions = questions,
                        )
                }
            } catch (e: Exception) {
                _uiState.value = MultipleChoiceUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun answerQuestion(selectedAnswer: String) {
        val currentState = _uiState.value as? MultipleChoiceUiState.Quiz ?: return
        val currentQuestion = currentState.currentQuestion ?: return

        val isCorrect = selectedAnswer == currentQuestion.correctAnswer
        val newScore = if (isCorrect) currentState.score + 1 else currentState.score
        val newIndex = currentState.currentQuestionIndex + 1

        val newAnsweredQuestions =
            currentState.answeredQuestions +
                (currentState.currentQuestionIndex to selectedAnswer)

        // Save failed question to user profile
        if (!isCorrect) {
            viewModelScope.launch {
                userPreferencesRepository.addFailedMultipleChoiceQuestion(currentQuestion.toString())
            }
        }

        _uiState.value =
            currentState.copy(
                currentQuestionIndex = newIndex,
                score = newScore,
                answeredQuestions = newAnsweredQuestions,
            )
    }

    fun restartQuiz() {
        val currentState = _uiState.value as? MultipleChoiceUiState.Quiz ?: return
        _uiState.value =
            currentState.copy(
                currentQuestionIndex = 0,
                score = 0,
                answeredQuestions = emptyMap(),
            )
    }

    fun getQuizResult(): MultipleChoiceResult? {
        val currentState = _uiState.value as? MultipleChoiceUiState.Quiz ?: return null
        return currentState.toResult()
    }
}
