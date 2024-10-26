package com.github.jvsena42.floresta.presentation.ui.screens.receive

import androidx.compose.runtime.Stable

@Stable
data class ReceiveUIState(
    val address: String = "",
    val bip21Uri: String? = null,
    val isLoading: Boolean = true
)
