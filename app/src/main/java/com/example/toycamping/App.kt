package com.example.toycamping

import android.app.Application
import com.example.toycamping.di.AppKoinSetup
import com.example.toycamping.di.KoinBaseKoinSetup

class App : Application() {

    private val appKoinSetup: KoinBaseKoinSetup = AppKoinSetup()

    override fun onCreate() {
        super.onCreate()
        appKoinSetup.setup(this)
    }
}