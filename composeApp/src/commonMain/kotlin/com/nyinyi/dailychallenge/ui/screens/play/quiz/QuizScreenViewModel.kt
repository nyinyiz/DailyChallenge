package com.nyinyi.dailychallenge.ui.screens.play.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizScreenViewModel(
    private val repository: ChallengesRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
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
                repository.getTrueFalseChallenges().let {
                    _state.value = QuizState.Success(it)
                }
            } catch (e: Exception) {
                _state.value = QuizState.Error
            }
        }
    }

    fun saveFailedQuestion(question: QuizCard) {
        viewModelScope.launch {
            userPreferencesRepository.addFailedQuizCard(question.toString())
        }
    }
}

sealed class QuizState {
    object Loading : QuizState()

    data class Success(
        val quizList: List<QuizCard>,
    ) : QuizState()

    object Error : QuizState()
}
