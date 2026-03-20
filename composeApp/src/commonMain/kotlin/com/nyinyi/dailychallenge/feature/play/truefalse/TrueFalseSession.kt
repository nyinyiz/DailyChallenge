package com.nyinyi.dailychallenge.feature.play.truefalse

import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.model.QuizResult

data class TrueFalseSession(
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

    fun answer(answer: Boolean): TrueFalseAnswerOutcome {
        val question = currentQuestion ?: return TrueFalseAnswerOutcome(nextSession = this)
        val failedQuestion = question.takeUnless { it.correctAnswer == answer }

        return TrueFalseAnswerOutcome(
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

    fun restart(): TrueFalseSession =
        copy(
            currentQuestionIndex = 0,
            incorrectAnswers = emptyList(),
        )
}

data class TrueFalseAnswerOutcome(
    val nextSession: TrueFalseSession,
    val failedQuestion: QuizCard? = null,
)
