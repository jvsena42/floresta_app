package com.github.jvsena42.floresta.presentation.ui.screens.node

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jvsena42.floresta.domain.floresta.FlorestaRpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NodeViewModel(
    private val florestaRpc: FlorestaRpc
): ViewModel() {

    private val _uiState = MutableStateFlow(NodeUiState())
    val uiState = _uiState.asStateFlow()

    private  fun getInfo() {
        viewModelScope.launch(Dispatchers.IO) {

        }

    }
}