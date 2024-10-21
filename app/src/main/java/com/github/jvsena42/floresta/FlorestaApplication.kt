package com.github.jvsena42.floresta

import android.app.Application
import com.github.jvsena42.floresta.presentation.MainViewmodel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class FlorestaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}

private val appModule = module {
    viewModel { MainViewmodel() }
}
