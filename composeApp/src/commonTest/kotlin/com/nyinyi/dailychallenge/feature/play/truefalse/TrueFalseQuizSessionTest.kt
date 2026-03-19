package com.nyinyi.dailychallenge.feature.play.truefalse

import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.ui.screens.play.quiz.TrueFalseQuizSession
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TrueFalseQuizSessionTest {
    @Test
    fun `answer tracks incorrect responses and advances session`() {
        val session =
            TrueFalseQuizSession(
                questions =
                    listOf(
                        QuizCard(
                            difficulty = "Easy",
                            question = "Compose is declarative",
                            correctAnswer = true,
                            explanation = "It is declarative.",
                        ),
                    ),
            )

        val outcome = session.answer(false)

        assertTrue(outcome.nextSession.isComplete)
        assertNotNull(outcome.failedQuestion)
        assertEquals(1, outcome.nextSession.incorrectAnswers.size)
    }

    @Test
    fun `restart clears progress`() {
        val session =
            TrueFalseQuizSession(
                questions =
                    listOf(
                        QuizCard("Easy", "Q1", true, "Because"),
                        QuizCard("Easy", "Q2", false, "Because"),
                    ),
                currentQuestionIndex = 2,
                incorrectAnswers = listOf(QuizCard("Easy", "Q1", true, "Because")),
            )

        val restarted = session.restart()

        assertEquals(0, restarted.currentQuestionIndex)
        assertTrue(restarted.incorrectAnswers.isEmpty())
        assertFalse(restarted.isComplete)
    }
}
