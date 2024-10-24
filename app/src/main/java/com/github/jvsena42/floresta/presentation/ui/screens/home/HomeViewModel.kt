package com.github.jvsena42.floresta.presentation.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.jvsena42.floresta.domain.bitcoin.WalletManager
import com.github.jvsena42.floresta.domain.bitcoin.WalletRepository
import com.github.jvsena42.floresta.presentation.util.formatInBtc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val walletRepository: WalletRepository,
    private val walletManager: WalletManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()

    private fun setup() {
        if (walletRepository.doesWalletExist()) {
            Log.d(TAG, "setup: Wallet exists")
            val balanceSats = walletManager.getBalance()
            _uiState.update { it.copy(
                balanceBTC = balanceSats.formatInBtc(),
                balanceSats = balanceSats.toString()
            ) }
        } else {
            Log.d(TAG, "setup: Wallet does not exists")
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}