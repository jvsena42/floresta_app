package com.github.jvsena42.floresta

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module

class FlorestaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}

private val appModule = module {

}
