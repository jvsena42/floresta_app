package com.github.jvsena42.floresta.presentation.util

import java.text.DecimalFormat

fun ULong?.formatInBtc(): String {
    val balanceInSats = if (this == 0UL || this == null) {
        0F
    } else {
        this.toFloat().div(100_000_000)
    }
    return DecimalFormat("0.00000000").format(balanceInSats)
}

//fun ULong?.toSats(): String {
//    val balanceInSats = if (this == 0UL || this == null) {
//        0F
//    } else {
//        this.toFloat().div(100_000_000)
//    }
//    return DecimalFormat("0.00000000").format(balanceInSats)
//}

fun String?.filterInternalBrackets() : String{
    if (this.isNullOrEmpty()) return ""

    return this.replace(Regex("""\[([^\[\]]*)\]"""), "$1")
}