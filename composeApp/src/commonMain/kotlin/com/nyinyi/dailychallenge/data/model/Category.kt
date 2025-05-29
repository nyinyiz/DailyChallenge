package com.nyinyi.dailychallenge.data.model

enum class Category {
    ANDROID,
    IOS,
    KOTLIN,
    SWIFT,
    FLUTTER;

    companion object {
        fun fromString(value: String): Category =
            values().find { it.name.equals(value, ignoreCase = true) } ?: ANDROID
    }
}