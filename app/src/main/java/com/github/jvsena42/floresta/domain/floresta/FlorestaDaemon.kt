package com.github.jvsena42.floresta.domain.floresta

import android.util.Log
import com.florestad.Florestad
import com.github.jvsena42.floresta.domain.bitcoin.WalletRepository
import org.bitcoindevkit.Descriptor
import org.rustbitcoin.bitcoin.Network
import com.florestad.Config
import com.florestad.Network as FlorestaNetwork
import kotlin.let

interface FlorestaDaemon {
    suspend fun start()
    suspend fun stop()
}

class FlorestaDaemonImpl(
    private val datadir: String,
    private val walletRepository: WalletRepository
) : FlorestaDaemon {

    var isRunning = false
    private lateinit var daemon: Florestad
    override suspend fun start() {
        Log.d(TAG, "start: ")
        if (isRunning) {
            Log.d(TAG, "start: Daemon already running")
            return
        }
        try {
            val descriptorList = walletRepository.getInitialWalletData().getOrNull()?.let {
                listOf(
                    Descriptor(
                        descriptor = it.descriptor,
                        Network.SIGNET
                    ).toString()
                )
            }
            Log.d(TAG, "start: descriptor list: $descriptorList")
            if (descriptorList.isNullOrEmpty()) {
                Log.w(TAG, "start: Empty descriptor list")
            }
            Log.d(TAG, "start: datadir: $datadir")
            val config = Config(
                dataDir = datadir,
                electrumAddress = ELECTRUM_ADDRESS,
                network = FlorestaNetwork.SIGNET,
                walletDescriptor = descriptorList?.firstOrNull()
            )
            daemon = Florestad.fromConfig(config)
            daemon.start().also {
                Log.i(TAG, "start: Floresta running with config $config")
                isRunning = true
            }
        } catch (e: Exception) {
            Log.e(TAG, "start error: ", e)
        }
    }

    override suspend fun stop() {
        if (!isRunning) return
        daemon.stop()
        isRunning = false
    }

    companion object {
        const val ELECTRUM_ADDRESS = "127.0.0.1:50001"
        private const val TAG = "FlorestaDaemonImpl"
    }
}