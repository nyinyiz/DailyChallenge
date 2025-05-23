package com.nyinyi.dailychallenge.data.model

data class DailyChallengeObj(
    val id: String,
    val question: String,
)

fun getDailyChallengeList(): List<DailyChallengeObj> {
    return listOf(
        DailyChallengeObj(
            "1",
            "You want to show a FloatingActionButton (FAB) only after scrolling past the first few items. You’ve got access to scroll state — now you need to connect that to visibility. How would you do it? Feel free to share code snippets!"
        ),
    )
}