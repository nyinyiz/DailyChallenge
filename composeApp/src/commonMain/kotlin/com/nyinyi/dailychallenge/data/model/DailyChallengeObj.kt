package com.nyinyi.dailychallenge.data.model

import dailychallenge.composeapp.generated.resources.Res
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Serializable
data class DailyChallengeObj(
    val id: String,
    val difficulty: String,
    // Easy, Medium, Hard
    val question: String,
    val questionCode: String = "",
    val answerCode: String = ""
)

private val defaultChallenges = listOf(
    DailyChallengeObj(
        id = "1",
        difficulty = "Easy",
        question = "You want to show a FloatingActionButton (FAB) only after scrolling past the first few items. You've got access to scroll state — now you need to connect that to visibility. How would you do it?",
        questionCode = "val listState = rememberLazyListState()\n\nLazyColumn(state = listState) {\n  items(100) { Text(\"Item \$it\") }\n}\n\n// Show a FAB only when the first visible index > 4",
        answerCode = "val listState = rememberLazyListState()\n\n// Solution: use derivedStateOf to efficiently compute visibility\nval showFab by remember {\n    derivedStateOf {\n        listState.firstVisibleItemIndex > 2\n    }\n}\n\n// Then use AnimatedVisibility with showFab to control the FAB\nAnimatedVisibility(\n    visible = showFab,\n    enter = fadeIn() + slideInVertically { it },\n    exit = fadeOut() + slideOutVertically { it }\n) {\n    FloatingActionButton(onClick = { /* Action */ }) {\n        // FAB content\n    }\n}"
    )
)

suspend fun getDailyChallengeList(): List<DailyChallengeObj> {
    return try {
        loadData()
    } catch (e: Exception) {
        println("Error parsing challenges JSON: ${e.message}")
        defaultChallenges
    }
}

@OptIn(ExperimentalResourceApi::class)
suspend fun loadData(): List<DailyChallengeObj> {
    val readBytes = Res.readBytes("files/daily_challenges.json")
    val jsonString = readBytes.decodeToString()
    return Json.decodeFromString(jsonString)
}
