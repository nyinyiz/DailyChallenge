package com.nyinyi.dailychallenge.ui.screens.profile.utils

import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.QuizCard
import kotlinx.serialization.json.Json

data class QuestionDisplay(
    val question: String,
    val answer: String?,
    val explanation: String?,
)

fun parseQuestionForDisplay(
    question: String,
    questionType: String,
): QuestionDisplay {
    val jsonParser =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    return try {
        println("Question: $questionType ==>>  $question")
        when (questionType) {
            "True/False Questions" -> {
                val quizCardJson =
                    convertQuizCardStringToJson(question)
                        ?: return QuestionDisplay(
                            "Error: Could not parse question. $question",
                            null,
                            null,
                        )
                val quizCard = jsonParser.decodeFromString<QuizCard>(quizCardJson)
                QuestionDisplay(
                    question = "Q: ${quizCard.question}",
                    answer = "Correct answer: ${quizCard.correctAnswer}",
                    explanation = "Explanation: ${quizCard.explanation}",
                )
            }

            "Multiple Choice Questions" -> {
                val mcqJson =
                    convertMultipleChoiceObjStringToJson(question)
                        ?: return QuestionDisplay("Error: Could not parse question.", null, null)
                val mcq = jsonParser.decodeFromString<MultipleChoiceObj>(mcqJson)
                QuestionDisplay(
                    question = "Q: ${mcq.question}",
                    answer = "Correct answer: ${mcq.correctAnswer}",
                    explanation = "Explanation: ${mcq.explanation}",
                )
            }

            "Multiple Select Questions" -> {
                val mcsObj =
                    convertMultipleSelectStringToJson(question)
                        ?: return QuestionDisplay(
                            "Error: Could not parse question. $question",
                            null,
                            null,
                        )
                QuestionDisplay(
                    question = "Q: ${mcsObj.question}",
                    answer = "Correct answers: ${mcsObj.correctAnswers.joinToString()}",
                    explanation = "Explanation: ${mcsObj.explanation}",
                )
            }

            else -> QuestionDisplay("Unknown question type: $question", null, null)
        }
    } catch (e: Exception) {
        println("FailedQuestionItem JSON parsing error: ${e.message} for type $questionType")
        QuestionDisplay("Error parsing question: $question", null, null)
    }
}

private fun convertQuizCardStringToJson(quizCardString: String): String? =
    try {
        val content = quizCardString.substringAfter("QuizCard(").substringBeforeLast(")")

        val difficultyStart = content.indexOf("difficulty=") + "difficulty=".length
        val difficultyEnd = content.indexOf(", question=")
        val difficulty = content.substring(difficultyStart, difficultyEnd)

        val questionStart = content.indexOf("question=") + "question=".length
        val questionEnd = content.indexOf(", correctAnswer=")
        val question = content.substring(questionStart, questionEnd)

        val correctAnswerStart = content.indexOf("correctAnswer=") + "correctAnswer=".length
        val correctAnswerEnd = content.indexOf(", explanation=")
        val correctAnswer = content.substring(correctAnswerStart, correctAnswerEnd).toBoolean()

        val explanationStart = content.indexOf("explanation=") + "explanation=".length
        val explanation = content.substring(explanationStart)

        """
        {
            "difficulty": "$difficulty",
            "question": "$question",
            "correctAnswer": $correctAnswer,
            "explanation": "$explanation"
        }
        """.trimIndent()
    } catch (e: Exception) {
        println("Error parsing QuizCard string: ${e.message}")
        null
    }

private fun convertMultipleSelectStringToJson(mcsString: String): MultipleSelectObj? =
    try {
        val content = mcsString.substringAfter("MultipleSelectObj(").substringBeforeLast(")")

        // Find question
        val questionStart = content.indexOf("question=") + "question=".length
        val questionEnd = content.indexOf(", options=")
        val question = content.substring(questionStart, questionEnd)

        // Find options (list)
        val optionsStart = content.indexOf("options=[") + "options=[".length
        val optionsEnd = content.indexOf("], correctAnswers=")
        val optionsStr = content.substring(optionsStart, optionsEnd)
        val options = optionsStr.split(", ").map { it.trim() }

        // Find correctAnswers (list)
        val correctAnswersStart = content.indexOf("correctAnswers=[") + "correctAnswers=[".length
        val correctAnswersEnd = content.indexOf("], difficulty=")
        val correctAnswersStr = content.substring(correctAnswersStart, correctAnswersEnd)
        val correctAnswers = correctAnswersStr.split(", ").map { it.trim() }

        // Find difficulty
        val difficultyStart = content.indexOf("difficulty=") + "difficulty=".length
        val difficultyEnd = content.indexOf(", explanation=")
        val difficulty = content.substring(difficultyStart, difficultyEnd)

        // Find explanation
        val explanationStart = content.indexOf("explanation=") + "explanation=".length
        val explanation = content.substring(explanationStart)

        MultipleSelectObj(
            question = question,
            options = options,
            correctAnswers = correctAnswers,
            difficulty = difficulty,
            explanation = explanation,
        )
    } catch (e: Exception) {
        println("Error parsing QuizCard string: ${e.message}")
        null
    }

private fun convertMultipleChoiceObjStringToJson(mcqString: String): String? =
    try {
        val pattern =
            Regex(
                """MultipleChoiceObj\(question=(.+?), options=(\[.+?\]), correctAnswer=(.+?), difficulty=(.+?), explanation=(.+)\)""",
            )

        val matchResult = pattern.find(mcqString)

        matchResult?.let { match ->
            val (question, optionsStr, correctAnswer, difficulty, explanation) = match.destructured

            val optionsList = parseOptionsFromString(optionsStr)
            val optionsJson = optionsList.joinToString(", ") { escapeJsonString(it) }

            """
            {
                "question": ${escapeJsonString(question)},
                "options": [$optionsJson],
                "correctAnswer": ${escapeJsonString(correctAnswer)},
                "difficulty": ${escapeJsonString(difficulty)},
                "explanation": ${escapeJsonString(explanation)}
            }
            """.trimIndent()
        }
    } catch (e: Exception) {
        println("Error converting MultipleChoiceObj string to JSON: ${e.message}")
        null
    }

private fun parseOptionsFromString(optionsStr: String): List<String> {
    val optionsPattern = Regex("""\[(.+)\]""")
    val optionsMatch = optionsPattern.find(optionsStr)

    return if (optionsMatch != null) {
        val optionsContent = optionsMatch.groupValues[1].trim()
        if (optionsContent.isBlank()) {
            emptyList()
        } else {
            parseCommaSeparatedList(optionsContent)
        }
    } else {
        emptyList()
    }
}

private fun parseCommaSeparatedList(content: String): List<String> {
    val result = mutableListOf<String>()
    var current = StringBuilder()
    var depth = 0

    content.forEach { char ->
        when (char) {
            ',' -> {
                if (depth == 0) {
                    result.add(current.toString().trim())
                    current = StringBuilder()
                } else {
                    current.append(char)
                }
            }

            '[', '(' -> {
                depth++
                current.append(char)
            }

            ']', ')' -> {
                depth--
                current.append(char)
            }

            else -> current.append(char)
        }
    }

    if (current.isNotEmpty()) {
        result.add(current.toString().trim())
    }

    return result
}

private fun escapeJsonString(str: String): String =
    "\"${
        str.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }\""
