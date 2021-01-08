package com.example.rfidtab

import android.app.Application
import com.example.rfidtab.di.viewModelModule
import com.example.rfidtab.service.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(getModule())
        }
    }

    private fun getModule(): List<Module> {
        return listOf(viewModelModule)
    }
}