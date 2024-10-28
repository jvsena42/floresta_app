package com.github.jvsena42.floresta.presentation.ui.screens.home

import com.github.jvsena42.floresta.domain.model.ChainPosition
import com.github.jvsena42.floresta.domain.model.TransactionDetails
import com.github.jvsena42.floresta.domain.model.TxType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TransactionVM(
    val title: String,
    val date: String,
    val amount: String,
    val isReceived: Boolean
)

fun TransactionDetails.toTransactionVM() = TransactionVM(
    title = this.txid,
    date = if (this.chainPosition is ChainPosition.Confirmed) convertMillisecondsToDateString(this.chainPosition.timestamp.toLong()) else "",
    amount = (if (txType == TxType.RECEIVE) this.received.toSat() else this.sent.toSat()).toString(),
    isReceived = txType == TxType.RECEIVE
)

private fun convertMillisecondsToDateString(milliseconds: Long): String {
    val date = Date(milliseconds)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}