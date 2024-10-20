package com.github.jvsena42.floresta.domain.bitcoin

import org.bitcoindevkit.Connection
import org.bitcoindevkit.Descriptor
import org.bitcoindevkit.DescriptorSecretKey
import org.bitcoindevkit.KeychainKind
import org.bitcoindevkit.Mnemonic
import org.bitcoindevkit.WordCount
import org.rustbitcoin.bitcoin.Network

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

    private fun initialize(
        descriptor: Descriptor,
        changeDescriptor: Descriptor,
    ) {
        wallet = org.bitcoindevkit.Wallet(
            descriptor,
            changeDescriptor,
            Network.SIGNET,
            dbConnection
        )
    }

    fun createWallet() {
        val mnemonic = Mnemonic(WordCount.WORDS12)
        val bip32ExtendedRootKey = DescriptorSecretKey(Network.SIGNET, mnemonic, null)
        val descriptor: Descriptor = Descriptor.newBip84(
            bip32ExtendedRootKey,
            KeychainKind.EXTERNAL,
            Network.SIGNET
        )

        val changeDescriptor: Descriptor = Descriptor.newBip84(
            bip32ExtendedRootKey,
            KeychainKind.INTERNAL,
            Network.SIGNET
        )

        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor
        )

        //TODO SAVE WALLET
        //TODO SAVE Mnemonic
    }

    companion object {
        private const val TAG = "WalletObject"
        private const val SIGNET_ELECTRUM_URL: String = "ssl://mempool.space:60602" //TODO IMPLEMENT FLORESTA URL
        private const val PERSISTENCE_VERSION = "V1"
    }
}