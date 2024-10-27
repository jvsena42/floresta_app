package com.github.jvsena42.floresta.domain.floresta

import android.util.Log
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class FlorestaRpcKtor {

    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val selectorManager = SelectorManager(Dispatchers.IO)
    lateinit var socket: Socket
    lateinit var sendChannel: ByteWriteChannel

    private val _response = MutableStateFlow("")
    val response = _response.asStateFlow()

    init {
        startSocket()
    }

    suspend fun getBlockchainInfo() {
        Log.d(TAG, "getBlockchainInfo: ")
        val arguments = JSONArray()

        val request = buildJsonRpcRequest(method = "getblockchaininfo", params = arguments)
        sendMessage(request)
    }

    private fun startSocket() = ioScope.launch {
        socket = aSocket(selectorManager).tcp().connect(ELECTRUM_ADDRESS, RCP_PORT)

        val receiveChannel = socket.openReadChannel()
        sendChannel = socket.openWriteChannel(autoFlush = true)

        while (true) {
            val response = receiveChannel.readUTF8Line()
            Log.d(TAG, "response: $response")
            _response.value = response.toString()
        }
    }

    private fun close() = ioScope.launch {
        socket.close()
        selectorManager.close()
    }

    private fun buildJsonRpcRequest(
        method: String,
        params: JSONArray
    ): String {
        return JSONObject().apply {
            put("jsonrpc", "2.0")
            put("method", method)
            put("params", params)
            put("id", 1)
        }.toString()
    }

    private suspend fun sendMessage(message: String) {
        Log.d(TAG, "sendMessage: $message")
        sendChannel.writeStringUtf8(message)
    }

    private companion object {
        const val TAG = "FlorestaRpcKtor"
        private const val ELECTRUM_ADDRESS = "127.0.0.1"
        private const val RCP_PORT = 38332
    }
}