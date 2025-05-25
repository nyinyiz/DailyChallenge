package com.nyinyi.dailychallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: ChallengesRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<AppState>(AppState.Loading)
    val state: StateFlow<AppState> = _state.asStateFlow()

    init {
        getDailyChallenges()
    }

    fun getDailyChallenges() {
        viewModelScope.launch {
            _state.value = AppState.Loading
            try {
                repository.getDailyChallenges().let {
                    _state.value = AppState.Content(it)
                }
            } catch (e: Exception) {
                _state.value = AppState.Error
            }
        }
    }

    fun getDailyChallengeById(id: String) {
        viewModelScope.launch {
            repository.getDailyChallengeById(id).let {
                _state.value = AppState.ContentById(it)
            }
        }
    }
}

sealed class AppState {
    object Loading : AppState()

    data class Content(
        val dailyChallenges: List<DailyChallengeObj>,
    ) : AppState()

    data class ContentById(
        val dailyChallenge: DailyChallengeObj,
    ) : AppState()

    object Error : AppState()
}
