package com.nyinyi.dailychallenge.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.nyinyi.dailychallenge.data.model.Category
import com.nyinyi.dailychallenge.data.model.FailedQuestionPayloadFormat
import com.nyinyi.dailychallenge.data.model.FailedQuestionRecord
import com.nyinyi.dailychallenge.data.model.FailedQuestionRecordCodec
import com.nyinyi.dailychallenge.data.model.FailedQuestionType
import com.nyinyi.dailychallenge.data.model.MatchingGameObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesRepository {
    private object PreferencesKeys {
        val SELECTED_CATEGORY = stringPreferencesKey("selected_category")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val DARK_THEME =
            androidx.datastore.preferences.core
                .booleanPreferencesKey("dark_theme")
        val FAILED_QUIZ_CARDS = stringSetPreferencesKey("failed_quiz_cards")
        val FAILED_MULTIPLE_CHOICE = stringSetPreferencesKey("failed_multiple_choice")
        val FAILED_MULTIPLE_SELECT = stringSetPreferencesKey("failed_multiple_select")
        val FAILED_MATCHING_GAME = stringSetPreferencesKey("failed_matching_game")
    }

    override val selectedCategory: Flow<Category> =
        dataStore.data.map { preferences ->
            try {
                val categoryName =
                    preferences[PreferencesKeys.SELECTED_CATEGORY] ?: Category.ANDROID.name
                Category.valueOf(categoryName)
            } catch (e: Exception) {
                Category.ANDROID
            }
        }

    override val userProfile: Flow<UserProfile> =
        dataStore.data.map { preferences ->
            val name = preferences[PreferencesKeys.USER_NAME] ?: ""
            val email = preferences[PreferencesKeys.USER_EMAIL] ?: ""
            val darkTheme = preferences[PreferencesKeys.DARK_THEME] ?: true
            val failedQuizCards =
                normalizeFailedRecords(
                    rawValues = preferences[PreferencesKeys.FAILED_QUIZ_CARDS],
                    expectedType = FailedQuestionType.TRUE_FALSE,
                )
            val multipleChoiceAndLegacyMatching =
                normalizeFailedRecords(
                    rawValues = preferences[PreferencesKeys.FAILED_MULTIPLE_CHOICE],
                    expectedType = FailedQuestionType.MULTIPLE_CHOICE,
                    allowLegacyMatching = true,
                )
            val failedMultipleChoice =
                multipleChoiceAndLegacyMatching.filter {
                    it.type == FailedQuestionType.MULTIPLE_CHOICE
                }
            val failedMultipleSelect =
                normalizeFailedRecords(
                    rawValues = preferences[PreferencesKeys.FAILED_MULTIPLE_SELECT],
                    expectedType = FailedQuestionType.MULTIPLE_SELECT,
                )
            val failedMatching =
                (
                    normalizeFailedRecords(
                        rawValues = preferences[PreferencesKeys.FAILED_MATCHING_GAME],
                        expectedType = FailedQuestionType.MATCHING_GAME,
                    ) + multipleChoiceAndLegacyMatching.filter {
                        it.type == FailedQuestionType.MATCHING_GAME
                    }
                ).distinctBy(FailedQuestionRecordCodec::encode)

            UserProfile(
                name = name,
                email = email,
                darkTheme = darkTheme,
                failedQuizCards = failedQuizCards,
                failedMultipleChoiceQuestions = failedMultipleChoice,
                failedMultipleSelectQuestions = failedMultipleSelect,
                failedMatchingGameQuestions = failedMatching,
            )
        }

    override suspend fun setSelectedCategory(category: Category) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CATEGORY] = category.name
        }
    }

    override suspend fun setUserProfile(userProfile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = userProfile.name
            preferences[PreferencesKeys.USER_EMAIL] = userProfile.email
            preferences[PreferencesKeys.DARK_THEME] = userProfile.darkTheme
            preferences[PreferencesKeys.FAILED_QUIZ_CARDS] =
                userProfile.failedQuizCards.map(FailedQuestionRecordCodec::encode).toSet()
            preferences[PreferencesKeys.FAILED_MULTIPLE_CHOICE] =
                userProfile.failedMultipleChoiceQuestions.map(FailedQuestionRecordCodec::encode).toSet()
            preferences[PreferencesKeys.FAILED_MULTIPLE_SELECT] =
                userProfile.failedMultipleSelectQuestions.map(FailedQuestionRecordCodec::encode).toSet()
            preferences[PreferencesKeys.FAILED_MATCHING_GAME] =
                userProfile.failedMatchingGameQuestions.map(FailedQuestionRecordCodec::encode).toSet()
        }
    }

    override suspend fun updateUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    override suspend fun updateUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_EMAIL] = email
        }
    }

    override suspend fun updateDarkTheme(darkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = darkTheme
        }
    }

    override suspend fun addFailedQuizCard(question: QuizCard) {
        addFailedRecord(
            key = PreferencesKeys.FAILED_QUIZ_CARDS,
            record = FailedQuestionRecordCodec.fromQuizCard(question),
        )
    }

    override suspend fun addFailedMultipleChoiceQuestion(question: MultipleChoiceObj) {
        addFailedRecord(
            key = PreferencesKeys.FAILED_MULTIPLE_CHOICE,
            record = FailedQuestionRecordCodec.fromMultipleChoice(question),
        )
    }

    override suspend fun addFailedMultipleSelectQuestion(question: MultipleSelectObj) {
        addFailedRecord(
            key = PreferencesKeys.FAILED_MULTIPLE_SELECT,
            record = FailedQuestionRecordCodec.fromMultipleSelect(question),
        )
    }

    override suspend fun addFailedMatchingGameQuestion(question: MatchingGameObj) {
        addFailedRecord(
            key = PreferencesKeys.FAILED_MATCHING_GAME,
            record = FailedQuestionRecordCodec.fromMatchingGame(question),
        )
    }

    override suspend fun clearFailedExercises() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.FAILED_QUIZ_CARDS)
            preferences.remove(PreferencesKeys.FAILED_MULTIPLE_CHOICE)
            preferences.remove(PreferencesKeys.FAILED_MULTIPLE_SELECT)
            preferences.remove(PreferencesKeys.FAILED_MATCHING_GAME)
        }
    }

    private suspend fun addFailedRecord(
        key: Preferences.Key<Set<String>>,
        record: FailedQuestionRecord,
    ) {
        dataStore.edit { preferences ->
            val currentFailed = preferences[key]?.toMutableSet() ?: mutableSetOf()
            currentFailed.add(FailedQuestionRecordCodec.encode(record))
            preferences[key] = currentFailed
        }
    }

    private fun normalizeFailedRecords(
        rawValues: Set<String>?,
        expectedType: FailedQuestionType,
        allowLegacyMatching: Boolean = false,
    ): List<FailedQuestionRecord> =
        rawValues
            .orEmpty()
            .mapNotNull { rawValue ->
                val decodedRecord = FailedQuestionRecordCodec.decode(rawValue)
                if (decodedRecord != null) {
                    return@mapNotNull decodedRecord
                }

                legacyRecordFromRawValue(
                    rawValue = rawValue,
                    expectedType = expectedType,
                    allowLegacyMatching = allowLegacyMatching,
                )
            }.distinctBy(FailedQuestionRecordCodec::encode)

    private fun legacyRecordFromRawValue(
        rawValue: String,
        expectedType: FailedQuestionType,
        allowLegacyMatching: Boolean,
    ): FailedQuestionRecord =
        when {
            rawValue.startsWith("QuizCard(") ->
                parseLegacyQuizCard(rawValue)?.let(FailedQuestionRecordCodec::fromQuizCard)
                    ?: FailedQuestionRecord(
                        type = FailedQuestionType.TRUE_FALSE,
                        payload = rawValue,
                        payloadFormat = FailedQuestionPayloadFormat.LEGACY_TEXT,
                    )

            rawValue.startsWith("MultipleChoiceObj(") ->
                parseLegacyMultipleChoice(rawValue)?.let(FailedQuestionRecordCodec::fromMultipleChoice)
                    ?: FailedQuestionRecord(
                        type = FailedQuestionType.MULTIPLE_CHOICE,
                        payload = rawValue,
                        payloadFormat = FailedQuestionPayloadFormat.LEGACY_TEXT,
                    )

            rawValue.startsWith("MultipleSelectObj(") ->
                parseLegacyMultipleSelect(rawValue)?.let(FailedQuestionRecordCodec::fromMultipleSelect)
                    ?: FailedQuestionRecord(
                        type = FailedQuestionType.MULTIPLE_SELECT,
                        payload = rawValue,
                        payloadFormat = FailedQuestionPayloadFormat.LEGACY_TEXT,
                    )

            allowLegacyMatching && rawValue.startsWith("MatchingGameObj(") ->
                parseLegacyMatchingGame(rawValue)?.let(FailedQuestionRecordCodec::fromMatchingGame)
                    ?: FailedQuestionRecord(
                        type = FailedQuestionType.MATCHING_GAME,
                        payload = rawValue,
                        payloadFormat = FailedQuestionPayloadFormat.LEGACY_TEXT,
                    )

            else ->
                FailedQuestionRecord(
                    type = expectedType,
                    payload = rawValue,
                    payloadFormat = FailedQuestionPayloadFormat.LEGACY_TEXT,
                )
        }

    private fun parseLegacyQuizCard(value: String): QuizCard? =
        runCatching {
            val content = value.substringAfter("QuizCard(").substringBeforeLast(")")
            val difficulty = content.substringAfter("difficulty=").substringBefore(", question=")
            val question = content.substringAfter("question=").substringBefore(", correctAnswer=")
            val correctAnswer =
                content.substringAfter("correctAnswer=").substringBefore(", explanation=").toBoolean()
            val explanation = content.substringAfter("explanation=")

            QuizCard(
                difficulty = difficulty,
                question = question,
                correctAnswer = correctAnswer,
                explanation = explanation,
            )
        }.getOrNull()

    private fun parseLegacyMultipleChoice(value: String): MultipleChoiceObj? =
        runCatching {
            val pattern =
                Regex(
                    """MultipleChoiceObj\(question=(.+?), options=(\[.+?\]), correctAnswer=(.+?), difficulty=(.+?), explanation=(.+)\)""",
                )
            val matchResult = pattern.find(value) ?: return@runCatching null
            val (question, optionsString, correctAnswer, difficulty, explanation) = matchResult.destructured

            MultipleChoiceObj(
                question = question,
                options = parseLegacyList(optionsString.removePrefix("[").removeSuffix("]")),
                correctAnswer = correctAnswer,
                difficulty = difficulty,
                explanation = explanation,
            )
        }.getOrNull()

    private fun parseLegacyMultipleSelect(value: String): MultipleSelectObj? =
        runCatching {
            val content = value.substringAfter("MultipleSelectObj(").substringBeforeLast(")")
            val question = content.substringAfter("question=").substringBefore(", options=")
            val options =
                parseLegacyList(
                    content.substringAfter("options=[").substringBefore("], correctAnswers="),
                )
            val correctAnswers =
                parseLegacyList(
                    content.substringAfter("correctAnswers=[").substringBefore("], difficulty="),
                )
            val difficulty = content.substringAfter("difficulty=").substringBefore(", explanation=")
            val explanation = content.substringAfter("explanation=")

            MultipleSelectObj(
                question = question,
                options = options,
                correctAnswers = correctAnswers,
                difficulty = difficulty,
                explanation = explanation,
            )
        }.getOrNull()

    private fun parseLegacyMatchingGame(value: String): MatchingGameObj? =
        null

    private fun parseLegacyList(content: String): List<String> {
        if (content.isBlank()) return emptyList()

        val result = mutableListOf<String>()
        var depth = 0
        val current = StringBuilder()

        content.forEach { char ->
            when (char) {
                ',' -> {
                    if (depth == 0) {
                        result.add(current.toString().trim())
                        current.clear()
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
}
