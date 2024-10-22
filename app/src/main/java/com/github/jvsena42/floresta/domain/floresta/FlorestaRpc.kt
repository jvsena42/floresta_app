package com.github.jvsena42.floresta.domain.floresta

interface FlorestaRpc {
    fun getBlockchainInfo()
    fun rescan()
    fun getProgress()
    fun stop()
    fun getPeerInfo()
}