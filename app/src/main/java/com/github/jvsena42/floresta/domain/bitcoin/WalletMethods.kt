package com.github.jvsena42.floresta.domain.bitcoin

import org.bitcoindevkit.Connection

class WalletMethods(
    private val dbPath: String,
) {

    private lateinit var dbConnection: Connection
    private lateinit var florestaDbPath: String
    private lateinit var wallet: org.bitcoindevkit.Wallet

    init {
        setPathAndConnectDb(dbPath)
    }

    private fun isWalletInitialized() = ::wallet.isInitialized

    private fun setPathAndConnectDb(path: String) {
        florestaDbPath = "$path/florestaDB_${PERSISTENCE_VERSION}.sqlite3"
        dbConnection = Connection(florestaDbPath)
    }

    companion object {
        private const val TAG = "WalletObject"
        private const val SIGNET_ELECTRUM_URL: String = "ssl://mempool.space:60602" //TODO IMPLEMENT FLORESTA URL
        private const val PERSISTENCE_VERSION = "V1"
    }
}