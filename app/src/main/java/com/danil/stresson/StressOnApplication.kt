package com.danil.stresson

import android.app.Application
import com.danil.stresson.data.AppContainer
import com.danil.stresson.data.DefaultAppContainer

class StressOnApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}