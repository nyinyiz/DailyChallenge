package com.nyinyi.dailychallenge.feature.play.multiselect

import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectResult

data class MultipleSelectSession(
    val questions: List<MultipleSelectObj>,
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val answeredQuestions: Map<Int, List<String>> = emptyMap(),
    val selectedOptions: List<String> = emptyList(),
) {
    val totalQuestions: Int = questions.size
    val currentQuestion: MultipleSelectObj? = questions.getOrNull(currentQuestionIndex)
    val isComplete: Boolean = currentQuestionIndex >= totalQuestions
    val difficultyStatus: String =
        currentQuestion
            ?.difficulty
            ?.takeIf { it.isNotBlank() }
            ?.lowercase()
            ?: "completed"
    val progressPercentage: Float =
        if (totalQuestions == 0) {
            0f
        } else {
            (currentQuestionIndex.toFloat() / totalQuestions.toFloat()) * 100
        }

    val result: MultipleSelectResult? =
        if (!isComplete) {
            null
        } else {
            MultipleSelectResult(
                score = score,
                totalQuestions = totalQuestions,
                incorrectAnswers =
                    questions.filterIndexed { index, question ->
                        val selectedAnswers = answeredQuestions[index] ?: emptyList()
                        !areAnswersCorrect(selectedAnswers, question.correctAnswers)
                    },
            )
        }

    fun toggleOption(option: String): MultipleSelectSession =
        copy(
            selectedOptions =
                if (selectedOptions.contains(option)) {
                    selectedOptions - option
                } else {
                    selectedOptions + option
                },
        )

    fun submitAnswer(): MultipleSelectSubmitOutcome {
        val question = currentQuestion ?: return MultipleSelectSubmitOutcome(nextSession = this)
        val isCorrect = areAnswersCorrect(selectedOptions, question.correctAnswers)

        return MultipleSelectSubmitOutcome(
            nextSession =
                copy(
                    currentQuestionIndex = currentQuestionIndex + 1,
                    score = if (isCorrect) score + 1 else score,
                    answeredQuestions = answeredQuestions + (currentQuestionIndex to selectedOptions),
                    selectedOptions = emptyList(),
                ),
            failedQuestion = question.takeUnless { isCorrect },
        )
    }

    fun restart(): MultipleSelectSession =
        copy(
            currentQuestionIndex = 0,
            score = 0,
            answeredQuestions = emptyMap(),
            selectedOptions = emptyList(),
        )

    private fun areAnswersCorrect(
        selected: List<String>,
        correct: List<String>,
    ): Boolean = selected.size == correct.size && selected.containsAll(correct)
}

data class MultipleSelectSubmitOutcome(
    val nextSession: MultipleSelectSession,
    val failedQuestion: MultipleSelectObj? = null,
)
