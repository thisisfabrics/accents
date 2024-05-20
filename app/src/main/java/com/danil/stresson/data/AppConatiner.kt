package com.danil.stresson.data

import android.content.Context

interface AppContainer {
    val stressRepository: StressRepository
}


class DefaultAppContainer(context: Context): AppContainer {
    override val stressRepository: StressRepository by lazy {
        StaticStressRepository(context)
    }
}
