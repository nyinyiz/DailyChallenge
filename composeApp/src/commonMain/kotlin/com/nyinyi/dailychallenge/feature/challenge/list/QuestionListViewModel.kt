package com.nyinyi.dailychallenge.feature.challenge.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.repository.ChallengeCatalogRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class QuestionListViewModel(
    private val repository: ChallengeCatalogRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<QuestionListState>(QuestionListState.Loading)
    val state: StateFlow<QuestionListState> = _state.asStateFlow()

    private val _eventChannel = Channel<QuestionListEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    init {
        getDailyChallenges()
    }

    fun getRandomChallenges() {
        viewModelScope.launch {
            try {
                repository.getRandomChallenge().let {
                    _eventChannel.send(QuestionListEvent.RandomDataChanged(it))
                }
            } catch (e: Exception) {
                _state.value = QuestionListState.Error
            }
        }
    }

    fun getDailyChallenges() {
        viewModelScope.launch {
            _state.value = QuestionListState.Loading
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

sealed class QuestionListEvent {
    data class RandomDataChanged(
        val randomChallenges: DailyChallengeObj,
    ) : QuestionListEvent()
}
