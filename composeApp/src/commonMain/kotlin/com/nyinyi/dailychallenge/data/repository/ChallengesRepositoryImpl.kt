package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.model.MatchingGameObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.QuizCard
import com.nyinyi.dailychallenge.data.remote.ChallengesApiService
import com.nyinyi.dailychallenge.data.remote.getOrDefault
import kotlinx.coroutines.flow.first

class ChallengesRepositoryImpl(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val apiService: ChallengesApiService,
) : ChallengesRepository {
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

    override suspend fun getDailyChallengeById(id: String): DailyChallengeObj {
        val result = apiService.getDailyChallenges()
        val challenges = result.getOrDefault(defaultChallenges)

        return try {
            challenges.first { it.id == id }
        } catch (e: Exception) {
            defaultChallenges.first()
        }
    }

    override suspend fun getDailyChallenges(): List<DailyChallengeObj> {
        val result = apiService.getDailyChallenges()
        return result.getOrDefault(defaultChallenges)
    }

    override suspend fun getRandomChallenges(): DailyChallengeObj {
        val dailyChallenges = getDailyChallenges()
        return dailyChallenges.random()
    }

    override suspend fun getTrueFalseChallenges(): List<QuizCard> {
        val category = userPreferencesRepository.selectedCategory.first()
        val result = apiService.getTrueFalseChallenges(category.name)
        val allQuestions = result.getOrDefault(emptyList())

        // Get 10 random questions
        return allQuestions
            .shuffled()
            .take(10)
    }

    override suspend fun getMultipleChoiceChallenges(): List<MultipleChoiceObj> {
        val category = userPreferencesRepository.selectedCategory.first()
        val result = apiService.getMultipleChoiceChallenges(category.name)
        val allQuestions = result.getOrDefault(emptyList())

        // Get 10 random questions
        return allQuestions
            .shuffled()
            .take(10)
    }

    override suspend fun getMultipleSelectChallenges(): List<MultipleSelectObj> {
        val category = userPreferencesRepository.selectedCategory.first()
        val result = apiService.getMultipleSelectChallenges(category.name)
        val allQuestions = result.getOrDefault(emptyList())

        // Get 10 random questions
        return allQuestions
            .shuffled()
            .take(10)
    }

    override suspend fun getMatchingGameChallenges(): List<MatchingGameObj> {
        val category = userPreferencesRepository.selectedCategory.first()
        val result = apiService.getMatchingGameChallenges(category.name)
        val allQuestions = result.getOrDefault(emptyList())

        // Get 3 random questions
        return allQuestions
            .shuffled()
            .take(3)
    }
}
