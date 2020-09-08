package com.dk.trellassignment

import android.app.Application
import com.dk.trellassignment.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TrellApp : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TrellApp)
            modules(appModules)
        }
    }
}