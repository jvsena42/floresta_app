package com.github.jvsena42.floresta.presentation.ui.screens.home

import androidx.compose.runtime.Stable

@Stable
data class HomeUIState(
    val balanceBTC: String = "",
    val balanceSats: String = "",
    val transactions: List<TransactionVM> = listOf()
)