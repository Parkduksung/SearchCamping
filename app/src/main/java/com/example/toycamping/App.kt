package com.example.toycamping

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.toycamping.di.AppKoinSetup
import com.example.toycamping.di.KoinBaseKoinSetup

class App : Application() {

    private val appKoinSetup: KoinBaseKoinSetup = AppKoinSetup()

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        appKoinSetup.setup(this)
        instance = this
    }

    fun context(): Context = applicationContext

    companion object {
        lateinit var instance: App
            private set
    }


}