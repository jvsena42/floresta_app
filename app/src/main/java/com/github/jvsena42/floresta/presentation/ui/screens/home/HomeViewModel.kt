package com.github.jvsena42.floresta.presentation.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jvsena42.floresta.domain.bitcoin.WalletManager
import com.github.jvsena42.floresta.domain.bitcoin.WalletRepository
import com.github.jvsena42.floresta.domain.floresta.FlorestaDaemon
import com.github.jvsena42.floresta.presentation.util.formatInBtc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class HomeViewModel(
    private val walletRepository: WalletRepository,
    private val walletManager: WalletManager,
    private val florestaDaemon: FlorestaDaemon
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        syncInLoop()
        if (walletRepository.doesWalletExist()) {
            Log.d(TAG, "mnemonic: ${walletRepository.getMnemonic().getOrNull()}")
        }
    }

    private suspend fun updateUI() {
        if (walletRepository.doesWalletExist()) {
            Log.d(TAG, "setup: Wallet exists")
            walletManager.loadWallet()
            val balanceSats = walletManager.getBalance()
            _uiState.update {
                it.copy(
                    balanceBTC = balanceSats.formatInBtc(),
                    balanceSats = balanceSats.toString()
                )
            }
        } else {
            Log.d(TAG, "setup: Wallet does not exists")
            walletManager.createWallet()
            florestaDaemon.restart()
            walletManager.loadWallet()
        }
    }

    private fun syncInLoop() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(5.seconds)
            updateUI()
            walletManager.sync()
            delay(5.seconds)
            syncInLoop()
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}