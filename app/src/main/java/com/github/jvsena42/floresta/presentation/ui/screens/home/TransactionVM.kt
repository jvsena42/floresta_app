package com.github.jvsena42.floresta.presentation.ui.screens.home

import android.text.format.DateFormat
import com.github.jvsena42.floresta.domain.model.ChainPosition
import com.github.jvsena42.floresta.domain.model.TransactionDetails
import com.github.jvsena42.floresta.domain.model.TxType
import java.util.Calendar
import java.util.Locale

data class TransactionVM(
    val title: String,
    val date: String,
    val amount: String,
    val isReceived: Boolean
)

fun TransactionDetails.toTransactionVM() = TransactionVM(
    title = this.txid,
    date = if (this.chainPosition is ChainPosition.Confirmed) this.chainPosition.timestamp.timestampToString() else "",
    amount = (if (txType == TxType.RECEIVE) this.received.toSat() else this.sent.toSat()).toString(),
    isReceived = txType == TxType.RECEIVE
)

fun ULong.timestampToString(): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = (this * 1000u).toLong()
    return DateFormat.format("MMMM d yyyy HH:mm", calendar).toString()
}