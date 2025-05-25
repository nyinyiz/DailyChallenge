package com.nyinyi.dailychallenge.di

import org.koin.core.context.startKoin

object KoinInitializer {
    fun init() {
        startKoin {
            modules(appModule)
        }
    }
}
