package com.example.keepall.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KeepAllApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KeepAllApp)
            modules(keepAllModules)
        }
    }
}