package com.nyinyi.dailychallenge

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
