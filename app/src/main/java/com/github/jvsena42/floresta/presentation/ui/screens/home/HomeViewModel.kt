package com.github.jvsena42.floresta.presentation.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jvsena42.floresta.data.FlorestaRpc
import com.github.jvsena42.floresta.domain.bitcoin.WalletManager
import com.github.jvsena42.floresta.domain.bitcoin.WalletRepository
import com.github.jvsena42.floresta.domain.floresta.FlorestaDaemon
import com.github.jvsena42.floresta.domain.model.ChainPosition
import com.github.jvsena42.floresta.presentation.util.formatInBtc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class HomeViewModel(
    private val walletRepository: WalletRepository,
    private val walletManager: WalletManager,
    private val florestaRpc: FlorestaRpc
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        syncInLoop()
        setup()
    }

    fun onAction(action: HomeActions) {
        when (action) {
            HomeActions.OnClickRefresh -> handleRefresh()
        }
    }

    private fun setup() = viewModelScope.launch(Dispatchers.IO) {
        if (walletRepository.doesWalletExist()) {
            Log.d(TAG, "mnemonic: ${walletRepository.getMnemonic().getOrNull()}")
            walletManager.loadWallet()
        }
    }

    private fun handleRefresh() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { it.copy(isRefreshEnabled = false) }
        florestaRpc.rescan().firstOrNull()
        delay(10.seconds)
        _uiState.update { it.copy(isRefreshEnabled = true) }
    }

    private suspend fun updateUI() {
        if (walletRepository.doesWalletExist()) {
            val balanceSats = walletManager.getBalance()
            val listTransactions = walletManager.listTransactions().map { it.toTransactionVM() }
            Log.d(TAG, "setup: Wallet exists. balance: $balanceSats")
            _uiState.update {
                it.copy(
                    balanceBTC = balanceSats.formatInBtc(),
                    balanceSats = balanceSats.toString(),
                    transactions = listTransactions
                )
            }
        } else {
            Log.d(TAG, "setup: Wallet does not exists")
//            walletManager.createWallet()
            walletManager.recoverWallet("bird unique ridge dose run problem scare label teach return inflict struggle")
        }
    }

    private fun syncInLoop() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(5.seconds)
            updateUI()
            walletManager.sync()
            syncInLoop()
        }
    }

    sealed interface HomeActions {
        data object OnClickRefresh : HomeActions
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}