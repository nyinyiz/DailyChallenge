package com.nyinyi.dailychallenge.ui.screens.play.matching

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinyi.dailychallenge.data.model.MatchingGameObj
import com.nyinyi.dailychallenge.data.model.MatchingGameResult
import com.nyinyi.dailychallenge.data.model.QuestionAttempt
import com.nyinyi.dailychallenge.data.repository.ChallengesRepository
import com.nyinyi.dailychallenge.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MatchingGameUiState {
    data object Loading : MatchingGameUiState()

    data class Error(
        val message: String,
    ) : MatchingGameUiState()

    data object Empty : MatchingGameUiState()

    data class Game(
        val questions: List<MatchingGameObj>,
        val currentQuestionIndex: Int = 0,
        val score: Int = 0,
        val leftItems: List<String> = emptyList(),
        val rightItems: List<String> = emptyList(),
        val selectedLeftIndex: Int? = null,
        val selectedRightIndex: Int? = null,
        val matchedPairs: Set<Pair<Int, Int>> = emptySet(),
        val incorrectAttempts: Int = 0,
    ) : MatchingGameUiState() {
        val totalQuestions: Int = questions.size
        val currentQuestion: MatchingGameObj? = questions.getOrNull(currentQuestionIndex)
        val isQuestionComplete: Boolean = matchedPairs.size == leftItems.size
        val isGameComplete: Boolean = currentQuestionIndex >= totalQuestions
        val progressPercentage: Float =
            (currentQuestionIndex.toFloat() / totalQuestions.toFloat()) * 100

        fun toResult(allQuestionAttempts: List<QuestionAttempt>): MatchingGameResult? {
            if (!isGameComplete) return null
            return MatchingGameResult(
                score = score,
                totalQuestions = totalQuestions,
                allQuestionAttempts = allQuestionAttempts,
            )
        }
    }
}

class MatchingGameViewModel(
    private val repository: ChallengesRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MatchingGameUiState>(MatchingGameUiState.Loading)
    val uiState: StateFlow<MatchingGameUiState> = _uiState.asStateFlow()

    private val questionAttempts = mutableMapOf<Int, QuestionAttempt>()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            try {
                val questions = repository.getMatchingGameChallenges()
                if (questions.isEmpty()) {
                    _uiState.value = MatchingGameUiState.Empty
                } else {
                    initializeQuestion(questions, 0, 0)
                }
            } catch (e: Exception) {
                _uiState.value = MatchingGameUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun initializeQuestion(
        questions: List<MatchingGameObj>,
        currentIndex: Int,
        currentScore: Int,
    ) {
        val question = questions.getOrNull(currentIndex) ?: return

        // Initialize question attempt if not exists
        if (!questionAttempts.containsKey(currentIndex)) {
            questionAttempts[currentIndex] =
                QuestionAttempt(
                    question = question,
                    incorrectAttempts = 0,
                    isCompleted = false,
                )
        }

        // Shuffle left and right items independently for difficulty
        val leftItems = question.pairs.map { it.left }.shuffled()
        val rightItems = question.pairs.map { it.right }.shuffled()

        _uiState.value =
            MatchingGameUiState.Game(
                questions = questions,
                currentQuestionIndex = currentIndex,
                score = currentScore,
                leftItems = leftItems,
                rightItems = rightItems,
                selectedLeftIndex = null,
                selectedRightIndex = null,
                matchedPairs = emptySet(),
                incorrectAttempts = questionAttempts[currentIndex]?.incorrectAttempts ?: 0,
            )
    }

    fun selectLeftItem(index: Int) {
        val currentState = _uiState.value as? MatchingGameUiState.Game ?: return

        // Check if this item is already matched
        if (currentState.matchedPairs.any { it.first == index }) return

        // If already selected, deselect
        if (currentState.selectedLeftIndex == index) {
            _uiState.value = currentState.copy(selectedLeftIndex = null)
            return
        }

        _uiState.value = currentState.copy(selectedLeftIndex = index)

        // If both items are selected, check for match
        currentState.selectedRightIndex?.let { rightIndex ->
            checkMatch(index, rightIndex)
        }
    }

    fun selectRightItem(index: Int) {
        val currentState = _uiState.value as? MatchingGameUiState.Game ?: return

        // Check if this item is already matched
        if (currentState.matchedPairs.any { it.second == index }) return

        // If already selected, deselect
        if (currentState.selectedRightIndex == index) {
            _uiState.value = currentState.copy(selectedRightIndex = null)
            return
        }

        _uiState.value = currentState.copy(selectedRightIndex = index)

        // If both items are selected, check for match
        currentState.selectedLeftIndex?.let { leftIndex ->
            checkMatch(leftIndex, index)
        }
    }

    private fun checkMatch(
        leftIndex: Int,
        rightIndex: Int,
    ) {
        val currentState = _uiState.value as? MatchingGameUiState.Game ?: return
        val currentQuestion = currentState.currentQuestion ?: return

        val leftItem = currentState.leftItems[leftIndex]
        val rightItem = currentState.rightItems[rightIndex]

        // Check if this is a correct pair
        val isCorrectMatch =
            currentQuestion.pairs.any { pair ->
                pair.left == leftItem && pair.right == rightItem
            }

        if (isCorrectMatch) {
            // Add to matched pairs
            val newMatchedPairs = currentState.matchedPairs + (leftIndex to rightIndex)
            val newScore = currentState.score + 1

            _uiState.value =
                currentState.copy(
                    selectedLeftIndex = null,
                    selectedRightIndex = null,
                    matchedPairs = newMatchedPairs,
                    score = newScore,
                )

            // Check if all pairs are matched
            if (newMatchedPairs.size == currentState.leftItems.size) {
                // Mark question as completed
                questionAttempts[currentState.currentQuestionIndex] =
                    questionAttempts[currentState.currentQuestionIndex]!!.copy(
                        isCompleted = true,
                    )

                // Question completed, move to next after a delay
                viewModelScope.launch {
                    kotlinx.coroutines.delay(500)
                    nextQuestion()
                }
            }
        } else {
            // Incorrect match
            val newIncorrectAttempts = currentState.incorrectAttempts + 1

            // Update question attempt
            questionAttempts[currentState.currentQuestionIndex] =
                questionAttempts[currentState.currentQuestionIndex]!!.copy(
                    incorrectAttempts = newIncorrectAttempts,
                )

            _uiState.value =
                currentState.copy(
                    selectedLeftIndex = null,
                    selectedRightIndex = null,
                    incorrectAttempts = newIncorrectAttempts,
                )

            // If too many incorrect attempts, save to user profile
            if (newIncorrectAttempts >= 3) {
                viewModelScope.launch {
                    userPreferencesRepository.addFailedMultipleChoiceQuestion(currentQuestion.toString())
                }
            }
        }
    }

    private fun nextQuestion() {
        val currentState = _uiState.value as? MatchingGameUiState.Game ?: return
        val nextIndex = currentState.currentQuestionIndex + 1

        if (nextIndex >= currentState.questions.size) {
            // Game completed
            _uiState.value =
                currentState.copy(
                    currentQuestionIndex = nextIndex,
                )
        } else {
            initializeQuestion(currentState.questions, nextIndex, currentState.score)
        }
    }

    fun restartGame() {
        val currentState = _uiState.value as? MatchingGameUiState.Game ?: return
        questionAttempts.clear()
        initializeQuestion(currentState.questions, 0, 0)
    }

    fun getGameResult(): MatchingGameResult? {
        val currentState = _uiState.value as? MatchingGameUiState.Game ?: return null
        val allAttempts = questionAttempts.values.toList()
        return currentState.toResult(allAttempts)
    }
}
