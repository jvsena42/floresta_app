package com.github.jvsena42.floresta.presentation.ui.screens.node

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jvsena42.floresta.domain.floresta.FlorestaRpcKtor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NodeViewModel(
    private val florestaRpc: FlorestaRpcKtor
): ViewModel() {

    private val _uiState = MutableStateFlow(NodeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getInfo()
    }

    private  fun getInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            florestaRpc.getBlockchainInfo()
            florestaRpc.response.collect { response ->
                Log.d(TAG, "getInfo response: $response")
            }
        }
    }

    private companion object {
        const val TAG = "NodeViewModel"
    }
}