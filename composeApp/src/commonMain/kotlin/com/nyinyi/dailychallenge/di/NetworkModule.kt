package com.nyinyi.dailychallenge.di

import com.nyinyi.dailychallenge.data.remote.ChallengesApiService
import com.nyinyi.dailychallenge.data.remote.logHttp
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule =
    module {
        single {
            HttpClient {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                    )
                }
                install(Logging) {
                    logger =
                        object : Logger {
                            override fun log(message: String) {
                                logHttp(message)
                            }
                        }
                    level = LogLevel.ALL
                }
            }
        }

        single { ChallengesApiService(get()) }
    }
