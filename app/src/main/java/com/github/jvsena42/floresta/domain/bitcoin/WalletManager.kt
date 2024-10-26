package com.github.jvsena42.floresta.domain.bitcoin

import android.util.Log
import com.github.jvsena42.floresta.domain.model.ChainPosition
import com.github.jvsena42.floresta.domain.model.Constants.PERSISTENCE_VERSION
import com.github.jvsena42.floresta.domain.model.TransactionDetails
import com.github.jvsena42.floresta.domain.model.TxType
import org.bitcoindevkit.Address
import org.bitcoindevkit.AddressInfo
import org.bitcoindevkit.Connection
import org.bitcoindevkit.Descriptor
import org.bitcoindevkit.DescriptorSecretKey
import org.bitcoindevkit.ElectrumClient
import org.bitcoindevkit.KeychainKind
import org.bitcoindevkit.Mnemonic
import org.bitcoindevkit.Psbt
import org.bitcoindevkit.Transaction
import org.bitcoindevkit.TxBuilder
import org.bitcoindevkit.Update
import org.bitcoindevkit.Wallet
import org.bitcoindevkit.WordCount
import org.rustbitcoin.bitcoin.Amount
import org.rustbitcoin.bitcoin.FeeRate
import org.rustbitcoin.bitcoin.Network
import org.bitcoindevkit.ChainPosition as BdkChainPosition

class WalletManager(
    dbPath: String,
    private val walletRepository: WalletRepository
) {

    private lateinit var dbConnection: Connection
    private lateinit var florestaDbPath: String
    private lateinit var wallet: Wallet
    private val blockchainClient: ElectrumClient by lazy { ElectrumClient(ELECTRUM_ADDRESS) }
    private var fullScanRequired: Boolean = true

    init {
        setPathAndConnectDb(dbPath)
        if (walletRepository.doesWalletExist()) {
            loadWallet()
        }
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
        return try {
            val result = walletRepository.getInitialWalletData().onFailure { e ->
                return@loadWallet Result.failure(e)
            }

            val data = result.getOrNull() ?: return Result.failure(Exception())

            val descriptor = Descriptor(data.descriptor, Network.SIGNET)
            val changeDescriptor = Descriptor(data.changeDescriptor, Network.SIGNET)


            wallet = Wallet.load(
                descriptor = descriptor,
                changeDescriptor = changeDescriptor,
                connection = dbConnection
            )

            return Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "loadWallet: ", e)
            return Result.failure(e)
        }
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

    fun getBalance(): ULong {
        return wallet.balance().total.toSat()
    }

    fun getLastUnusedAddress(): AddressInfo {
        return wallet.revealNextAddress(KeychainKind.EXTERNAL)
    }

    fun createPartiallySignedTransaction(
        recipientAddress: String,
        amount: Amount,
        feeRate: FeeRate
    ): Psbt {
        val recipientScriptPubKey = Address(recipientAddress, Network.SIGNET).scriptPubkey()
        return TxBuilder()
            .addRecipient(recipientScriptPubKey, amount)
            .feeRate(feeRate)
            .finish(wallet)
    }

    fun sign(partiallySignedTransaction: Psbt) {
        wallet.sign(partiallySignedTransaction)
    }

    fun listTransactions(): List<TransactionDetails> {
        return wallet.transactions().map { tx ->
            val (sent, received) = wallet.sentAndReceived(tx.transaction)
            val fee = wallet.calculateFee(tx.transaction)
            val feeRate = wallet.calculateFeeRate(tx.transaction)
            val txType: TxType = txType(sent = sent.toSat(), received = received.toSat())
            val chainPosition: ChainPosition = when (val position = tx.chainPosition) {
                is BdkChainPosition.Unconfirmed -> ChainPosition.Unconfirmed
                is BdkChainPosition.Confirmed -> ChainPosition.Confirmed(
                    position.confirmationBlockTime.blockId.height,
                    position.confirmationBlockTime.confirmationTime
                )
            }

            TransactionDetails(
                txid = tx.transaction.computeTxid(),
                sent = sent,
                received = received,
                fee = fee,
                feeRate = feeRate,
                txType = txType,
                chainPosition = chainPosition
            )
        }
    }

    fun getTransaction(txid: String): TransactionDetails? {
        val allTransactions = listTransactions()
        allTransactions.forEach {
            if (it.txid == txid) {
                return it
            }
        }
        return null
    }

    fun broadcast(tx: Transaction): String {
        blockchainClient.broadcast(tx)
        return tx.computeTxid()
    }

    fun txType(sent: ULong, received: ULong): TxType {
        return if (sent > received) TxType.PAYMENT else TxType.RECEIVE
    }

    companion object {
        private const val TAG = "WalletManager"
        const val ELECTRUM_ADDRESS = "127.0.0.1:50001"
    }
}