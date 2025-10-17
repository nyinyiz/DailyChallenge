package com.nyinyi.dailychallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProgrammingTip(
    val id: String,
    val category: String,
    val tip: String,
)
