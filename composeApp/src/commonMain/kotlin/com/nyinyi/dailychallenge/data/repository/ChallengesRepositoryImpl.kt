package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.QuizCard
import dailychallenge.composeapp.generated.resources.Res
import kotlinx.serialization.json.Json

class ChallengesRepositoryImpl : ChallengesRepository {
    private val defaultChallenges =
        listOf(
            DailyChallengeObj(
                id = "1",
                difficulty = "Easy",
                question = "You want to show a FloatingActionButton (FAB) only after scrolling past the first few items. You've got access to scroll state — now you need to connect that to visibility. How would you do it?",
                questionCode = "val listState = rememberLazyListState()\n\nLazyColumn(state = listState) {\n  items(100) { Text(\"Item \$it\") }\n}\n\n// Show a FAB only when the first visible index > 4",
                answerCode = "val listState = rememberLazyListState()\n\n// Solution: use derivedStateOf to efficiently compute visibility\nval showFab by remember {\n    derivedStateOf {\n        listState.firstVisibleItemIndex > 2\n    }\n}\n\n// Then use AnimatedVisibility with showFab to control the FAB\nAnimatedVisibility(\n    visible = showFab,\n    enter = fadeIn() + slideInVertically { it },\n    exit = fadeOut() + slideOutVertically { it }\n) {\n    FloatingActionButton(onClick = { /* Action */ }) {\n        // FAB content\n    }\n}",
            ),
        )

    override suspend fun getDailyChallengeById(id: String): DailyChallengeObj =
        try {
            val readBytes = Res.readBytes("files/daily_challenges.json")
            val jsonString = readBytes.decodeToString()
            val challenges = Json.decodeFromString<List<DailyChallengeObj>>(jsonString)
            challenges.first { it.id == id }
        } catch (e: Exception) {
            println("Error parsing challenges JSON: ${e.message}")
            defaultChallenges.first()
        }

    override suspend fun getDailyChallenges(): List<DailyChallengeObj> {
        return try {
            val readBytes = Res.readBytes("files/daily_challenges.json")
            val jsonString = readBytes.decodeToString()
            return Json.decodeFromString(jsonString)
        } catch (e: Exception) {
            println("Error parsing challenges JSON: ${e.message}")
            defaultChallenges
        }
    }

    override suspend fun getTrueFalseChallenges(): List<QuizCard> =
        try {
            val readBytes = Res.readBytes("files/true_or_false_challenges_2.json")
            val jsonString = readBytes.decodeToString()
            val allQuestions: List<QuizCard> = Json.decodeFromString(jsonString)

            // Get 10 random questions
            allQuestions
                .shuffled() // Randomly shuffle the list
                .take(10) // Take first 10 items from shuffled list
                .also { randomQuestions ->
                    if (randomQuestions.isEmpty()) {
                        println("Warning: No questions were loaded")
                    } else {
                        println("Successfully loaded ${randomQuestions.size} random questions")
                    }
                }
        } catch (e: Exception) {
            println("Error loading questions: ${e.message}")
            emptyList()
        }

    override suspend fun getMultipleChoiceChallenges(): List<MultipleChoiceObj> =
        try {
            val readBytes = Res.readBytes("files/multiple_choice_challenges_android.json")
            val jsonString = readBytes.decodeToString()
            val allQuestions: List<MultipleChoiceObj> = Json.decodeFromString(jsonString)

            // Get 10 random questions
            allQuestions
                .shuffled() // Randomly shuffle the list
                .take(10) // Take first 10 items from shuffled list
                .also { randomQuestions ->
                    if (randomQuestions.isEmpty()) {
                        println("Warning: No questions were loaded")
                    } else {
                        println("Successfully loaded ${randomQuestions.size} random questions")
                    }
                }
        } catch (e: Exception) {
            println("Error loading questions: ${e.message}")
            emptyList()
        }
}
