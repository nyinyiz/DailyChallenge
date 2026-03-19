package com.nyinyi.dailychallenge.feature.challenge.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.repository.ChallengeCatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionDetailViewModel(
    private val challengesRepository: ChallengeCatalogRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<QuestionDetailUiState>(QuestionDetailUiState.Loading)
    val state: StateFlow<QuestionDetailUiState> = _state.asStateFlow()

    private var loadedQuestionId: String? = null

    fun loadQuestion(id: String) {
        if (loadedQuestionId == id && _state.value is QuestionDetailUiState.Success) {
            return
        }

        loadedQuestionId = id
        viewModelScope.launch {
            _state.value = QuestionDetailUiState.Loading
            try {
                val question = challengesRepository.getDailyChallengeById(id)
                _state.value = QuestionDetailUiState.Success(question)
            } catch (e: Exception) {
                _state.value =
                    QuestionDetailUiState.Error(
                        message = e.message ?: "Unable to load challenge details.",
                    )
            }
        }
    }
}

sealed interface QuestionDetailUiState {
    data object Loading : QuestionDetailUiState

    data class Success(
        val question: DailyChallengeObj,
    ) : QuestionDetailUiState

    data class Error(
        val message: String,
    ) : QuestionDetailUiState
}
