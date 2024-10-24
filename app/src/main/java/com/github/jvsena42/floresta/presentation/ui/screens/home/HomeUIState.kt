package com.github.jvsena42.floresta.presentation.ui.screens.home

class HomeUIState(
    val balanceBTC: String,
    val balanceSats: String,
    val transactions: List<TransactionVM> = listOf()
)