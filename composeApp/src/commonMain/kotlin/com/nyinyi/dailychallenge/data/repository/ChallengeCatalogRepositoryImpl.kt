package com.nyinyi.dailychallenge.data.repository

import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.remote.ChallengesApiService
import com.nyinyi.dailychallenge.data.remote.getOrDefault

class ChallengeCatalogRepositoryImpl(
    private val apiService: ChallengesApiService,
) : ChallengeCatalogRepository {
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
        val challenges = apiService.getDailyChallenges().getOrDefault(defaultChallenges)
        return challenges.firstOrNull { it.id == id } ?: defaultChallenges.first()
    }

    override suspend fun getDailyChallenges(): List<DailyChallengeObj> =
        apiService.getDailyChallenges().getOrDefault(defaultChallenges)

    override suspend fun getRandomChallenge(): DailyChallengeObj = getDailyChallenges().random()
}
