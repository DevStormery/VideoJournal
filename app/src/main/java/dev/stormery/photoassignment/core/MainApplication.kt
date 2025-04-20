package dev.stormery.photoassignment.core

import android.app.Application
import dev.stormery.photoassignment.di.dataModule
import dev.stormery.photoassignment.di.domainModule
import dev.stormery.photoassignment.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@MainApplication)
            modules(listOf(presentationModule, domainModule, dataModule))
        }
    }
}