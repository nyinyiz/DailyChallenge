package com.nyinyi.dailychallenge.data.remote

import android.util.Log

private const val TAG = "DailyChallenge_HTTP"

/**
 * Android implementation of HTTP logger using Android's Log class.
 * All HTTP logs will appear in logcat with tag "DailyChallenge_HTTP"
 */
actual fun logHttp(message: String) {
    Log.d(TAG, message)
}
