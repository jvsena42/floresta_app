package com.github.jvsena42.floresta.presentation.ui.screens.node

import androidx.compose.runtime.Stable

@Stable
data class NodeUiState(
    val numberOfPeers: String = "",
    val blockHeight: String = "",
    val blockHash: String = "",
    val network: String = "",
    val difficulty: String = "",
)