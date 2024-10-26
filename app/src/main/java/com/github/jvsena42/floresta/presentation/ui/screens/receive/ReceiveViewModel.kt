package com.github.jvsena42.floresta.presentation.ui.screens.receive

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jvsena42.floresta.domain.bitcoin.WalletManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReceiveViewModel(
    private val walletManager: WalletManager
): ViewModel() {

    private val _uiState = MutableStateFlow(ReceiveUIState())
    val uiState = _uiState.asStateFlow()

    init {
        updateAddress()
    }

    private fun updateAddress() = viewModelScope.launch {
        val address = walletManager.getLastUnusedAddress()
        delay(500)
        Log.d(TAG, "updateAddress: Address: ${address.address} uri: ${address.address.toQrUri()}")
        _uiState.update { it.copy(address = address.address.toString(), bip21Uri = address.address.toQrUri(), isLoading = false) }
    }

    private companion object {
        private const val TAG = "ReceiveViewModel"
    }
}