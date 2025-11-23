package com.example.bookstoremvvmcoroutines

import android.app.Application
import com.example.bookstoremvvmcoroutines.dependencyinjection.appModules
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModules)
        }
    }
}