package com.nyinyi.dailychallenge.data.remote

/**
 * iOS implementation of HTTP logger using println.
 */
actual fun logHttp(message: String) {
    println("[DailyChallenge_HTTP] $message")
}
