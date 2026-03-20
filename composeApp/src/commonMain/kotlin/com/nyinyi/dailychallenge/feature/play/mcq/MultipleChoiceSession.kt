package com.nyinyi.dailychallenge.feature.play.mcq

import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceResult

data class MultipleChoiceSession(
    val questions: List<MultipleChoiceObj>,
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val answeredQuestions: Map<Int, String> = emptyMap(),
) {
    val totalQuestions: Int = questions.size
    val currentQuestion: MultipleChoiceObj? = questions.getOrNull(currentQuestionIndex)
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

    val result: MultipleChoiceResult? =
        if (!isComplete) {
            null
        } else {
            MultipleChoiceResult(
                score = score,
                totalQuestions = totalQuestions,
                incorrectAnswers =
                    questions.filterIndexed { index, question ->
                        answeredQuestions[index] != question.correctAnswer
                    },
            )
        }

    fun answer(selectedAnswer: String): MultipleChoiceAnswerOutcome {
        val question = currentQuestion ?: return MultipleChoiceAnswerOutcome(nextSession = this)
        val isCorrect = selectedAnswer == question.correctAnswer

        return MultipleChoiceAnswerOutcome(
            nextSession =
                copy(
                    currentQuestionIndex = currentQuestionIndex + 1,
                    score = if (isCorrect) score + 1 else score,
                    answeredQuestions = answeredQuestions + (currentQuestionIndex to selectedAnswer),
                ),
            failedQuestion = question.takeUnless { isCorrect },
        )
    }

    fun restart(): MultipleChoiceSession =
        copy(
            currentQuestionIndex = 0,
            score = 0,
            answeredQuestions = emptyMap(),
        )
}

data class MultipleChoiceAnswerOutcome(
    val nextSession: MultipleChoiceSession,
    val failedQuestion: MultipleChoiceObj? = null,
)
