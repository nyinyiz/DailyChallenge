package com.nyinyi.dailychallenge.ui.screens.play.quiz

import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.model.QuizResult

data class TrueFalseQuizSession(
    val questions: List<QuizCard>,
    val currentQuestionIndex: Int = 0,
    val incorrectAnswers: List<QuizCard> = emptyList(),
) {
    val totalQuestions: Int = questions.size
    val currentQuestion: QuizCard? = questions.getOrNull(currentQuestionIndex)
    val isComplete: Boolean = currentQuestionIndex >= totalQuestions
    val difficultyStatus: String =
        currentQuestion
            ?.difficulty
            ?.takeIf { it.isNotBlank() }
            ?: "completed"

    val result: QuizResult? =
        if (!isComplete) {
            null
        } else {
            QuizResult(
                totalQuestions = totalQuestions,
                correctAnswers = totalQuestions - incorrectAnswers.size,
                incorrectAnswers = incorrectAnswers,
            )
        }

    fun answer(answer: Boolean): TrueFalseQuizAnswerOutcome {
        val question = currentQuestion ?: return TrueFalseQuizAnswerOutcome(nextSession = this)
        val failedQuestion = question.takeUnless { it.correctAnswer == answer }

        return TrueFalseQuizAnswerOutcome(
            nextSession =
                copy(
                    currentQuestionIndex = currentQuestionIndex + 1,
                    incorrectAnswers =
                        if (failedQuestion != null) {
                            incorrectAnswers + failedQuestion
                        } else {
                            incorrectAnswers
                        },
                ),
            failedQuestion = failedQuestion,
        )
    }

    fun restart(): TrueFalseQuizSession =
        copy(
            currentQuestionIndex = 0,
            incorrectAnswers = emptyList(),
        )
}

data class TrueFalseQuizAnswerOutcome(
    val nextSession: TrueFalseQuizSession,
    val failedQuestion: QuizCard? = null,
)
