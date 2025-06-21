package com.nyinyi.dailychallenge.ui.screens.play.multiselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectResult
import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
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
        val questions: List<MultipleSelectObj>,
        val currentQuestionIndex: Int = 0,
        val score: Int = 0,
        val answeredQuestions: Map<Int, List<String>> = emptyMap(),
        val selectedOptions: List<String> = emptyList(),
    ) : MultipleSelectUiState() {
        val totalQuestions: Int = questions.size
        val currentQuestion: MultipleSelectObj? = questions.getOrNull(currentQuestionIndex)
        val isComplete: Boolean = currentQuestionIndex >= totalQuestions
        val progressPercentage: Float =
            (currentQuestionIndex.toFloat() / totalQuestions.toFloat()) * 100

        fun toResult(): MultipleSelectResult? {
            if (!isComplete) return null
            return MultipleSelectResult(
                score = score,
                totalQuestions = totalQuestions,
                incorrectAnswers =
                    questions.filterIndexed { index, question ->
                        val selectedAnswers = answeredQuestions[index] ?: emptyList()
                        !areAnswersCorrect(selectedAnswers, question.correctAnswers)
                    },
            )
        }
        
        private fun areAnswersCorrect(selected: List<String>, correct: List<String>): Boolean {
            // Check if selected answers match exactly with correct answers
            return selected.size == correct.size && selected.containsAll(correct)
        }
    }
}

class MultipleSelectViewModel(
    private val repository: ChallengesRepository,
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
                            questions = questions,
                        )
                }
            } catch (e: Exception) {
                _uiState.value = MultipleSelectUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun toggleOptionSelection(option: String) {
        val currentState = _uiState.value as? MultipleSelectUiState.Quiz ?: return
        
        val currentSelectedOptions = currentState.selectedOptions.toMutableList()
        if (currentSelectedOptions.contains(option)) {
            currentSelectedOptions.remove(option)
        } else {
            currentSelectedOptions.add(option)
        }
        
        _uiState.value = currentState.copy(selectedOptions = currentSelectedOptions)
    }

    fun submitAnswer() {
        val currentState = _uiState.value as? MultipleSelectUiState.Quiz ?: return
        val currentQuestion = currentState.currentQuestion ?: return
        val selectedOptions = currentState.selectedOptions
        
        val isCorrect = areAnswersCorrect(selectedOptions, currentQuestion.correctAnswers)
        val newScore = if (isCorrect) currentState.score + 1 else currentState.score
        val newIndex = currentState.currentQuestionIndex + 1
        
        val newAnsweredQuestions =
            currentState.answeredQuestions +
                (currentState.currentQuestionIndex to selectedOptions)
        
        _uiState.value =
            currentState.copy(
                currentQuestionIndex = newIndex,
                score = newScore,
                answeredQuestions = newAnsweredQuestions,
                selectedOptions = emptyList(), // Reset selected options for next question
            )
    }
    
    private fun areAnswersCorrect(selected: List<String>, correct: List<String>): Boolean {
        // Check if selected answers match exactly with correct answers
        return selected.size == correct.size && selected.containsAll(correct)
    }

    fun restartQuiz() {
        val currentState = _uiState.value as? MultipleSelectUiState.Quiz ?: return
        _uiState.value =
            currentState.copy(
                currentQuestionIndex = 0,
                score = 0,
                answeredQuestions = emptyMap(),
                selectedOptions = emptyList(),
            )
    }

    fun getQuizResult(): MultipleSelectResult? {
        val currentState = _uiState.value as? MultipleSelectUiState.Quiz ?: return null
        return currentState.toResult()
    }
}