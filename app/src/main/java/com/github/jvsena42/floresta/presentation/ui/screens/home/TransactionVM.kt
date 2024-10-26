package com.github.jvsena42.floresta.presentation.ui.screens.home

data class TransactionVM(
    val title: String,
    val date: String,
    val amount: String,
    val isReceived: Boolean
)