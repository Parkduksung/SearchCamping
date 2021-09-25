package com.example.toycamping.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

abstract class KoinBaseKoinSetup {

    protected abstract fun getModules(): List<Module>


    private fun createModules(): List<Module> {
        return mutableListOf<Module>().apply {
            addAll(getModules())
        }
    }

    fun setup(context: Context) {
        startKoin {
            androidContext(context.applicationContext)
            modules(createModules())
        }
    }

}