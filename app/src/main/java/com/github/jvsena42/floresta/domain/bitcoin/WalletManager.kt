package com.github.jvsena42.floresta.domain.bitcoin

import android.util.Log
import org.bitcoindevkit.Connection
import org.bitcoindevkit.Descriptor
import org.bitcoindevkit.DescriptorSecretKey
import org.bitcoindevkit.ElectrumClient
import org.bitcoindevkit.KeychainKind
import org.bitcoindevkit.Mnemonic
import org.bitcoindevkit.Update
import org.bitcoindevkit.Wallet
import org.bitcoindevkit.WordCount
import org.rustbitcoin.bitcoin.Network

class WalletManager(
    dbPath: String,
    private val walletRepository: WalletRepository
) {

    private lateinit var dbConnection: Connection
    private lateinit var florestaDbPath: String
    private lateinit var wallet: Wallet
    private val blockchainClient: ElectrumClient by lazy { ElectrumClient(SIGNET_ELECTRUM_URL) }
    private var fullScanRequired: Boolean = false

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
            path = florestaDbPath,
            descriptor = descriptor.toStringWithSecret(),
            changeDescriptor = changeDescriptor.toStringWithSecret()
        )

        walletRepository.saveMnemonic(mnemonic.toString())
    }

    fun loadWallet(): Result<Unit> {
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

    fun recoverWallet(recoveryPhrase: String) {
        val mnemonic = Mnemonic.fromString(recoveryPhrase)
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
            changeDescriptor = changeDescriptor,
        )

        walletRepository.saveWallet(
            florestaDbPath,
            descriptor.toStringWithSecret(),
            changeDescriptor.toStringWithSecret()
        )
        walletRepository.saveMnemonic(mnemonic.toString())
    }

    private fun fullScan() {
        val fullScanRequest = wallet.startFullScan().build()
        val update: Update = blockchainClient.fullScan(
            fullScanRequest = fullScanRequest,
            stopGap = 100u,
            batchSize = 10u,
            fetchPrevTxouts = true
        )
        wallet.applyUpdate(update)
        wallet.persist(dbConnection)
    }

    fun sync() {
        if (fullScanRequired) {
            Log.d(TAG, "sync: fullScanRequired")
            fullScan()
            fullScanRequired = false
        } else {
            Log.d(TAG, "sync: normal sync")
            val syncRequest = wallet.startSyncWithRevealedSpks().build()
            val update = blockchainClient.sync(
                syncRequest = syncRequest,
                batchSize = 10u,
                fetchPrevTxouts = true
            )
            wallet.applyUpdate(update)
            wallet.persist(dbConnection)
        }
    }



    companion object {
        private const val TAG = "WalletObject"
        private const val SIGNET_ELECTRUM_URL: String =
            "ssl://mempool.space:60602" //TODO IMPLEMENT FLORESTA URL
        const val PERSISTENCE_VERSION = "V1"
    }
}