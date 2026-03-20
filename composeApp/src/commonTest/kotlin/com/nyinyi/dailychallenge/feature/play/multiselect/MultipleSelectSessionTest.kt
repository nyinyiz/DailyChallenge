package com.nyinyi.dailychallenge.feature.play.multiselect

import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MultipleSelectSessionTest {
    @Test
    fun `toggle option adds and removes selection`() {
        val session =
            MultipleSelectSession(
                questions =
                    listOf(
                        MultipleSelectObj(
                            question = "Select DI tools",
                            options = listOf("Koin", "Hilt", "Ktor"),
                            correctAnswers = listOf("Koin", "Hilt"),
                            difficulty = "Medium",
                            explanation = "Koin and Hilt are DI tools.",
                        ),
                    ),
            )

        val selected = session.toggleOption("Koin").toggleOption("Hilt")
        val deselected = selected.toggleOption("Koin")

        assertEquals(listOf("Koin", "Hilt"), selected.selectedOptions)
        assertEquals(listOf("Hilt"), deselected.selectedOptions)
    }

    @Test
    fun `submit answer clears selection and records success`() {
        val session =
            MultipleSelectSession(
                questions =
                    listOf(
                        MultipleSelectObj(
                            question = "Select DI tools",
                            options = listOf("Koin", "Hilt", "Ktor"),
                            correctAnswers = listOf("Koin", "Hilt"),
                            difficulty = "Medium",
                            explanation = "Koin and Hilt are DI tools.",
                        ),
                    ),
            ).toggleOption("Koin").toggleOption("Hilt")

        val outcome = session.submitAnswer()

        assertNull(outcome.failedQuestion)
        assertEquals(1, outcome.nextSession.score)
        assertTrue(outcome.nextSession.selectedOptions.isEmpty())
        assertTrue(outcome.nextSession.isComplete)
    }
}
