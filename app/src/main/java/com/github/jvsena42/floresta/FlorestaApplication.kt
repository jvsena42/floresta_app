package com.github.jvsena42.floresta

import android.app.Application
import android.content.Context
import com.github.jvsena42.floresta.domain.bitcoin.WalletManager
import com.github.jvsena42.floresta.domain.bitcoin.WalletRepository
import com.github.jvsena42.floresta.domain.bitcoin.WalletRepositoryImpl
import com.github.jvsena42.floresta.presentation.MainViewmodel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class FlorestaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                domainModule,
                presentationModule
            )
        }
    }
}

private val presentationModule = module {
    viewModel { MainViewmodel() }
}
private val domainModule = module {
    single { WalletManager(androidApplication().filesDir.toString()) }
    single<WalletRepository> {
        WalletRepositoryImpl(
            androidApplication().getSharedPreferences(
                "wallet",
                Context.MODE_PRIVATE
            )
        )
    }
}
