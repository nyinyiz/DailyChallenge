package com.nyinyi.dailychallenge.ui.screens.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class QuestionListViewModel(
    private val repository: ChallengesRepository,
) : ViewModel() {
    private val _currentScreen = mutableStateOf<BottomNavItem>(BottomNavItem.Home)
    val currentScreen = _currentScreen

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
                repository.getRandomChallenges().let {
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

    fun updateCurrentScreen(screen: BottomNavItem) {
        _currentScreen.value = screen
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
