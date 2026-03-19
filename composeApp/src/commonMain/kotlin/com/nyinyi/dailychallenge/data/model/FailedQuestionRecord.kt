package com.nyinyi.dailychallenge.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
enum class FailedQuestionType {
    TRUE_FALSE,
    MULTIPLE_CHOICE,
    MULTIPLE_SELECT,
    MATCHING_GAME,
}

@Serializable
enum class FailedQuestionPayloadFormat {
    SERIALIZED_MODEL,
    LEGACY_TEXT,
}

@Serializable
data class FailedQuestionRecord(
    val type: FailedQuestionType,
    val payload: String,
    val payloadFormat: FailedQuestionPayloadFormat = FailedQuestionPayloadFormat.SERIALIZED_MODEL,
)

object FailedQuestionRecordCodec {
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }

    fun encode(record: FailedQuestionRecord): String = json.encodeToString(FailedQuestionRecord.serializer(), record)

    fun decode(value: String): FailedQuestionRecord? =
        runCatching {
            json.decodeFromString(FailedQuestionRecord.serializer(), value)
        }.getOrNull()

    fun fromQuizCard(question: QuizCard): FailedQuestionRecord =
        FailedQuestionRecord(
            type = FailedQuestionType.TRUE_FALSE,
            payload = json.encodeToString(QuizCard.serializer(), question),
        )

    fun fromMultipleChoice(question: MultipleChoiceObj): FailedQuestionRecord =
        FailedQuestionRecord(
            type = FailedQuestionType.MULTIPLE_CHOICE,
            payload = json.encodeToString(MultipleChoiceObj.serializer(), question),
        )

    fun fromMultipleSelect(question: MultipleSelectObj): FailedQuestionRecord =
        FailedQuestionRecord(
            type = FailedQuestionType.MULTIPLE_SELECT,
            payload = json.encodeToString(MultipleSelectObj.serializer(), question),
        )

    fun fromMatchingGame(question: MatchingGameObj): FailedQuestionRecord =
        FailedQuestionRecord(
            type = FailedQuestionType.MATCHING_GAME,
            payload = json.encodeToString(MatchingGameObj.serializer(), question),
        )

    fun decodeQuizCard(record: FailedQuestionRecord): QuizCard? =
        if (record.type == FailedQuestionType.TRUE_FALSE &&
            record.payloadFormat == FailedQuestionPayloadFormat.SERIALIZED_MODEL
        ) {
            runCatching { json.decodeFromString(QuizCard.serializer(), record.payload) }.getOrNull()
        } else {
            null
        }

    fun decodeMultipleChoice(record: FailedQuestionRecord): MultipleChoiceObj? =
        if (record.type == FailedQuestionType.MULTIPLE_CHOICE &&
            record.payloadFormat == FailedQuestionPayloadFormat.SERIALIZED_MODEL
        ) {
            runCatching { json.decodeFromString(MultipleChoiceObj.serializer(), record.payload) }.getOrNull()
        } else {
            null
        }

    fun decodeMultipleSelect(record: FailedQuestionRecord): MultipleSelectObj? =
        if (record.type == FailedQuestionType.MULTIPLE_SELECT &&
            record.payloadFormat == FailedQuestionPayloadFormat.SERIALIZED_MODEL
        ) {
            runCatching { json.decodeFromString(MultipleSelectObj.serializer(), record.payload) }.getOrNull()
        } else {
            null
        }

    fun decodeMatchingGame(record: FailedQuestionRecord): MatchingGameObj? =
        if (record.type == FailedQuestionType.MATCHING_GAME &&
            record.payloadFormat == FailedQuestionPayloadFormat.SERIALIZED_MODEL
        ) {
            runCatching { json.decodeFromString(MatchingGameObj.serializer(), record.payload) }.getOrNull()
        } else {
            null
        }
}
