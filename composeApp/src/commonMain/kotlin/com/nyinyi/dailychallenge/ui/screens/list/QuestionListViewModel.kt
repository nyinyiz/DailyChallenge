package com.nyinyi.dailychallenge.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionListViewModel(
    private val repository: ChallengesRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<QuestionListState>(QuestionListState.Loading)
    val state: StateFlow<QuestionListState> = _state.asStateFlow()

    init {
        getDailyChallenges()
    }

    fun getDailyChallenges() {
        viewModelScope.launch {
            _state.value = QuestionListState.Loading
            delay(1000)
            try {
                repository.getDailyChallenges().let {
                    _state.value = QuestionListState.Success(it)
                }
            } catch (e: Exception) {
                _state.value = QuestionListState.Error
            }
        }
    }
}

sealed class QuestionListState {
    object Loading : QuestionListState()

    data class Success(
        val dailyChallenges: List<DailyChallengeObj>,
    ) : QuestionListState()

    object Error : QuestionListState()
}
