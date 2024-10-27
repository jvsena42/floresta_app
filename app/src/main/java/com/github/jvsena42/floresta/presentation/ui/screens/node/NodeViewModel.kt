package com.github.jvsena42.floresta.presentation.ui.screens.node

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jvsena42.floresta.data.FlorestaRpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class NodeViewModel(
    private val florestaRpc: FlorestaRpc
): ViewModel() {

    private val _uiState = MutableStateFlow(NodeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getInLoop()
    }

    private fun getInLoop() {
        viewModelScope.launch(Dispatchers.IO) {
            getInfo()
            delay(3.seconds)
            getInLoop()
        }
    }

    private fun getInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            florestaRpc.getBlockchainInfo().collect { result ->
                result.onSuccess { data ->
                    Log.d(TAG, "getBlockchainInfo: $data")
                    _uiState.update { it.copy(
                        blockHeight = data.result.height.toString(),
                        difficulty = data.result.difficulty.toString(),
                        network = data.result.chain.uppercase(),
                        blockHash = data.result.bestBlock
                    ) }
                }
            }
            florestaRpc.getPeerInfo().collect { result ->
                Log.d(TAG, "getPeerInfo: ${result.getOrNull()}")
            }
        }
    }

    private companion object {
        const val TAG = "NodeViewModel"
    }
}