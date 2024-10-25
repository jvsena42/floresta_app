package com.github.jvsena42.floresta

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.github.jvsena42.floresta.domain.bitcoin.WalletManager
import com.github.jvsena42.floresta.domain.bitcoin.WalletRepository
import com.github.jvsena42.floresta.domain.bitcoin.WalletRepositoryImpl
import com.github.jvsena42.floresta.domain.floresta.FlorestaDaemon
import com.github.jvsena42.floresta.domain.floresta.FlorestaDaemonImpl
import com.github.jvsena42.floresta.domain.floresta.FlorestaService
import com.github.jvsena42.floresta.presentation.ui.screens.main.MainViewmodel
import com.github.jvsena42.floresta.presentation.ui.screens.home.HomeViewModel
import com.github.jvsena42.floresta.presentation.ui.screens.receive.ReceiveViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class FlorestaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FlorestaApplication)
            modules(
                domainModule,
                presentationModule
            )
        }
        try {
            startForegroundService(Intent(this, FlorestaService::class.java))
        } catch (e: Exception) {
            Log.e("FlorestaApplication", "onCreate: ", e)
        }
    }
}

val presentationModule = module {
    viewModel { MainViewmodel() }
    viewModel {
        HomeViewModel(
            walletRepository = get<WalletRepository>(),
            walletManager = get(),
            florestaDaemon = get()
        )
        ReceiveViewModel(walletManager = get())
    }
}

val domainModule = module {
    single {
        WalletManager(
            dbPath = androidContext().filesDir.toString(),
            walletRepository = get()
        )
    }
    single<WalletRepository> {
        WalletRepositoryImpl(
            androidContext().getSharedPreferences(
                "wallet",
                Context.MODE_PRIVATE
            )
        )
    }
    single<FlorestaDaemon> {
        FlorestaDaemonImpl(
            datadir = androidContext().filesDir.toString(),
            walletRepository = get()
        )
    }
}
