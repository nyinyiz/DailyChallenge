package com.nyinyi.dailychallenge.feature.play.mcq

import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MultipleChoiceSessionTest {
    @Test
    fun `answer increments score on correct answer`() {
        val session =
            MultipleChoiceSession(
                questions =
                    listOf(
                        MultipleChoiceObj(
                            question = "What handles DI?",
                            options = listOf("Koin", "Retrofit"),
                            correctAnswer = "Koin",
                            difficulty = "Easy",
                            explanation = "Koin is DI.",
                        ),
                    ),
            )

        val outcome = session.answer("Koin")

        assertEquals(1, outcome.nextSession.score)
        assertNull(outcome.failedQuestion)
        assertTrue(outcome.nextSession.isComplete)
        assertNotNull(outcome.nextSession.result)
    }

    @Test
    fun `answer records failed question on incorrect answer`() {
        val question =
            MultipleChoiceObj(
                question = "What handles DI?",
                options = listOf("Koin", "Retrofit"),
                correctAnswer = "Koin",
                difficulty = "Easy",
                explanation = "Koin is DI.",
            )

        val outcome = MultipleChoiceSession(questions = listOf(question)).answer("Retrofit")

        assertEquals(question, outcome.failedQuestion)
        assertEquals(0, outcome.nextSession.score)
        assertEquals(listOf(question), outcome.nextSession.result?.incorrectAnswers)
    }
}
