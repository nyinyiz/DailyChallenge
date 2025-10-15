package com.nyinyi.dailychallenge.data.remote

sealed class NetworkResult<out T> {
    data class Success<T>(
        val data: T,
    ) : NetworkResult<T>()

    data class NetworkError(
        val message: String,
        val exception: Exception? = null,
    ) : NetworkResult<Nothing>()

    data class Error(
        val message: String,
        val exception: Exception? = null,
    ) : NetworkResult<Nothing>()
}

fun <T> NetworkResult<T>.getOrNull(): T? =
    when (this) {
        is NetworkResult.Success -> data
        else -> null
    }

fun <T> NetworkResult<T>.getOrDefault(default: T): T =
    when (this) {
        is NetworkResult.Success -> data
        else -> default
    }

fun <T> NetworkResult<T>.isSuccess(): Boolean = this is NetworkResult.Success

fun <T> NetworkResult<T>.isError(): Boolean = this is NetworkResult.Error || this is NetworkResult.NetworkError
