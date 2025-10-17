package com.nyinyi.dailychallenge.widget.data

import android.content.Context
import com.nyinyi.dailychallenge.data.model.ProgrammingTip
import dailychallenge.composeapp.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class TipsRepository(
    private val context: Context,
) {
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    suspend fun getAllTips(): List<ProgrammingTip> =
        withContext(Dispatchers.IO) {
            try {
                val readBytes = Res.readBytes("files/programming_tips.json")
                val jsonString = readBytes.decodeToString()
                json.decodeFromString<List<ProgrammingTip>>(jsonString)
            } catch (e: Exception) {
                listOf(
                    ProgrammingTip(
                        id = "fallback",
                        category = "Programming Knowledge",
                        tip = "Write clean, maintainable code. Future you will thank present you!",
                    ),
                )
            }
        }

    suspend fun getRandomTip(): ProgrammingTip = getAllTips().randomOrNull() ?: getDefaultTip()

    private fun getDefaultTip(): ProgrammingTip =
        ProgrammingTip(
            id = "default",
            category = "Best Practices",
            tip = "Keep learning and coding every day!",
        )
}
