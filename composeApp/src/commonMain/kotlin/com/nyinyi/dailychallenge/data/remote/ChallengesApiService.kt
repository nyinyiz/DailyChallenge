package com.nyinyi.dailychallenge.data.remote

import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.model.MatchingGameObj
import com.nyinyi.dailychallenge.data.model.MultipleChoiceObj
import com.nyinyi.dailychallenge.data.model.MultipleSelectObj
import com.nyinyi.dailychallenge.data.model.ProgrammingTip
import com.nyinyi.dailychallenge.data.model.QuizCard
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException

class ChallengesApiService(
    private val httpClient: HttpClient,
) {
    suspend fun getDailyChallenges(): NetworkResult<List<DailyChallengeObj>> =
        safeApiCall {
            val url = ApiConstants.BASE_URL + ApiConstants.DAILY_CHALLENGES
            val response: HttpResponse = httpClient.get(url)

            if (response.status.isSuccess()) {
                val challenges: List<DailyChallengeObj> = response.body()
                NetworkResult.Success(challenges)
            } else {
                NetworkResult.Error("Failed to fetch daily challenges: ${response.status.description}")
            }
        }

    suspend fun getTrueFalseChallenges(category: String): NetworkResult<List<QuizCard>> =
        safeApiCall {
            val url = ApiConstants.BASE_URL + ApiConstants.getTrueFalseChallengesEndpoint(category)
            val response: HttpResponse = httpClient.get(url)

            if (response.status.isSuccess()) {
                val challenges: List<QuizCard> = response.body()
                NetworkResult.Success(challenges)
            } else {
                NetworkResult.Error("Failed to fetch true/false challenges: ${response.status.description}")
            }
        }

    suspend fun getMultipleChoiceChallenges(category: String): NetworkResult<List<MultipleChoiceObj>> =
        safeApiCall {
            val url = ApiConstants.BASE_URL + ApiConstants.getMultipleChoiceChallengesEndpoint(category)
            val response: HttpResponse = httpClient.get(url)

            if (response.status.isSuccess()) {
                val challenges: List<MultipleChoiceObj> = response.body()
                NetworkResult.Success(challenges)
            } else {
                NetworkResult.Error("Failed to fetch multiple choice challenges: ${response.status.description}")
            }
        }

    suspend fun getMultipleSelectChallenges(category: String): NetworkResult<List<MultipleSelectObj>> =
        safeApiCall {
            val url = ApiConstants.BASE_URL + ApiConstants.getMultipleSelectChallengesEndpoint(category)
            val response: HttpResponse = httpClient.get(url)

            if (response.status.isSuccess()) {
                val challenges: List<MultipleSelectObj> = response.body()
                NetworkResult.Success(challenges)
            } else {
                NetworkResult.Error("Failed to fetch multiple select challenges: ${response.status.description}")
            }
        }

    suspend fun getMatchingGameChallenges(category: String): NetworkResult<List<MatchingGameObj>> =
        safeApiCall {
            val url = ApiConstants.BASE_URL + ApiConstants.getMatchingGameChallengesEndpoint(category)
            val response: HttpResponse = httpClient.get(url)

            if (response.status.isSuccess()) {
                val challenges: List<MatchingGameObj> = response.body()
                NetworkResult.Success(challenges)
            } else {
                NetworkResult.Error("Failed to fetch matching game challenges: ${response.status.description}")
            }
        }

    suspend fun getProgrammingTips(): NetworkResult<List<ProgrammingTip>> =
        safeApiCall {
            val url = ApiConstants.BASE_URL + ApiConstants.PROGRAMMING_TIPS
            val response: HttpResponse = httpClient.get(url)

            if (response.status.isSuccess()) {
                val tips: List<ProgrammingTip> = response.body()
                NetworkResult.Success(tips)
            } else {
                NetworkResult.Error("Failed to fetch programming tips: ${response.status.description}")
            }
        }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> NetworkResult<T>): NetworkResult<T> =
        try {
            apiCall()
        } catch (e: SerializationException) {
            NetworkResult.Error(
                message = "Failed to parse response data: ${e.message}",
                exception = e,
            )
        } catch (e: io.ktor.client.network.sockets.ConnectTimeoutException) {
            NetworkResult.NetworkError(
                message = "Connection timeout. Please check your internet connection.",
                exception = e,
            )
        } catch (e: io.ktor.client.network.sockets.SocketTimeoutException) {
            NetworkResult.NetworkError(
                message = "Request timeout. Please try again.",
                exception = e,
            )
        } catch (e: io.ktor.client.plugins.ClientRequestException) {
            NetworkResult.Error(
                message = "Request failed: ${e.response.status.description}",
                exception = e,
            )
        } catch (e: io.ktor.client.plugins.ServerResponseException) {
            NetworkResult.Error(
                message = "Server error: ${e.response.status.description}",
                exception = e,
            )
        } catch (e: Exception) {
            val isNetworkError =
                e.message?.contains("network", ignoreCase = true) == true ||
                    e.message?.contains("connection", ignoreCase = true) == true ||
                    e.message?.contains("host", ignoreCase = true) == true

            if (isNetworkError) {
                NetworkResult.NetworkError(
                    message = "Unable to connect to server. Please check your internet connection.",
                    exception = e,
                )
            } else {
                NetworkResult.Error(
                    message = "An unexpected error occurred: ${e.message}",
                    exception = e,
                )
            }
        }
}
