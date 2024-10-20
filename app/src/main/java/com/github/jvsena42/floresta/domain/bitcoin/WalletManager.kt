package com.github.jvsena42.floresta.domain.bitcoin

import org.bitcoindevkit.Connection
import org.bitcoindevkit.Descriptor
import org.bitcoindevkit.DescriptorSecretKey
import org.bitcoindevkit.KeychainKind
import org.bitcoindevkit.Mnemonic
import org.bitcoindevkit.Wallet
import org.bitcoindevkit.WordCount
import org.rustbitcoin.bitcoin.Network

class WalletManager(
    private val dbPath: String,
    private val walletRepository: WalletRepository
) {

    private lateinit var dbConnection: Connection
    private lateinit var florestaDbPath: String
    private lateinit var wallet: Wallet

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
        wallet = Wallet(
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

        walletRepository.saveWallet(
            path = dbPath,
            descriptor = descriptor.toStringWithSecret(),
            changeDescriptor = changeDescriptor.toStringWithSecret()
        )

        walletRepository.saveMnemonic(mnemonic.toString())
    }

    fun loadWallet() : Result<Unit> {
        val result = walletRepository.getInitialWalletData().onFailure { e ->
            return@loadWallet Result.failure(e)
        }

        val data = result.getOrNull() ?: return Result.failure(Exception())

        val descriptor = Descriptor(data.descriptor, Network.SIGNET)
        val changeDescriptor = Descriptor(data.descriptor, Network.SIGNET)

        wallet = Wallet.load(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
            connection = dbConnection
        )

        return Result.success(Unit)
    }

    companion object {
        private const val TAG = "WalletObject"
        private const val SIGNET_ELECTRUM_URL: String = "ssl://mempool.space:60602" //TODO IMPLEMENT FLORESTA URL
        const val PERSISTENCE_VERSION = "V1"
    }
}