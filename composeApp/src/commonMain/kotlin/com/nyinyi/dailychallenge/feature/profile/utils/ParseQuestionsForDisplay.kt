package com.nyinyi.dailychallenge.feature.profile.utils

import com.nyinyi.dailychallenge.data.model.FailedQuestionPayloadFormat
import com.nyinyi.dailychallenge.data.model.FailedQuestionRecord
import com.nyinyi.dailychallenge.data.model.FailedQuestionRecordCodec
import com.nyinyi.dailychallenge.data.model.FailedQuestionType
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.QuizCard

data class QuestionDisplay(
    val question: String,
    val answer: String?,
    val explanation: String?,
)

fun questionDisplayForRecord(record: FailedQuestionRecord): QuestionDisplay =
    when {
        record.payloadFormat == FailedQuestionPayloadFormat.LEGACY_TEXT ->
            QuestionDisplay(
                question = record.payload,
                answer = null,
                explanation = "Saved from an older app version. Details may be limited.",
            )

        record.type == FailedQuestionType.TRUE_FALSE ->
            FailedQuestionRecordCodec.decodeQuizCard(record)?.toDisplay()
                ?: fallbackDisplay(record)

        record.type == FailedQuestionType.MULTIPLE_CHOICE ->
            FailedQuestionRecordCodec.decodeMultipleChoice(record)?.toDisplay()
                ?: fallbackDisplay(record)

        record.type == FailedQuestionType.MULTIPLE_SELECT ->
            FailedQuestionRecordCodec.decodeMultipleSelect(record)?.toDisplay()
                ?: fallbackDisplay(record)

        record.type == FailedQuestionType.MATCHING_GAME ->
            FailedQuestionRecordCodec.decodeMatchingGame(record)?.let { question ->
                QuestionDisplay(
                    question = "Q: ${question.question}",
                    answer = "Pairs: ${question.pairs.joinToString { "${it.left} -> ${it.right}" }}",
                    explanation = "Explanation: ${question.explanation}",
                )
            } ?: fallbackDisplay(record)

        else -> fallbackDisplay(record)
    }

fun reportDetailsForRecord(record: FailedQuestionRecord): String =
    when (record.payloadFormat) {
        FailedQuestionPayloadFormat.SERIALIZED_MODEL -> record.payload
        FailedQuestionPayloadFormat.LEGACY_TEXT -> record.payload
    }

private fun QuizCard.toDisplay(): QuestionDisplay =
    QuestionDisplay(
        question = "Q: $question",
        answer = "Correct answer: $correctAnswer",
        explanation = "Explanation: $explanation",
    )

private fun MultipleChoiceObj.toDisplay(): QuestionDisplay =
    QuestionDisplay(
        question = "Q: $question",
        answer = "Correct answer: $correctAnswer",
        explanation = "Explanation: $explanation",
    )

private fun MultipleSelectObj.toDisplay(): QuestionDisplay =
    QuestionDisplay(
        question = "Q: $question",
        answer = "Correct answers: ${correctAnswers.joinToString()}",
        explanation = "Explanation: $explanation",
    )

private fun fallbackDisplay(record: FailedQuestionRecord): QuestionDisplay =
    QuestionDisplay(
        question = "Unable to fully restore this saved question.",
        answer = null,
        explanation = record.payload,
    )
