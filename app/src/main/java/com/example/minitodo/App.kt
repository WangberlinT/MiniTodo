package com.example.minitodo

import android.app.Application
import com.example.minitodo.di.Injector

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }
}