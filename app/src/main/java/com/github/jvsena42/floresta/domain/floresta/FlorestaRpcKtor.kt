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
import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.time.Duration.Companion.seconds

class FlorestaRpcKtor {

    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var selectorManager: SelectorManager? = null
    private var socket: Socket? = null
    private var sendChannel: ByteWriteChannel? = null
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
        selectorManager = SelectorManager(Dispatchers.IO)

        while (isActive) {
            try {
                Log.d(TAG, "Connecting to socket...")
                socket = aSocket(selectorManager!!).tcp().connect(ELECTRUM_ADDRESS, RCP_PORT)
                sendChannel = socket!!.openWriteChannel(autoFlush = true)
                val receiveChannel = socket!!.openReadChannel()

                Log.d(TAG, "Socket connected successfully.")

                // Start reading responses
                while (isActive) {
                    val response = receiveChannel.readUTF8Line()
                    if (response == null) {
                        Log.e(TAG, "Connection closed by server.")
                        break
                    }
                    Log.d(TAG, "Received response: $response")
                    _response.value = response
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error in socket connection: ", e)
                delay(2.seconds)  // Retry connection after delay
            } finally {
                closeSocket()
            }
        }
    }

    private fun closeSocket() {
        Log.d(TAG, "Closing socket...")
        socket?.close()
        selectorManager?.close()
        sendChannel = null
        socket = null
        selectorManager = null
    }

    private fun close() {
        ioScope.launch {
            closeSocket()
            ioScope.cancel()
        }
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
        if ((socket == null) || (sendChannel == null)) {
            delay(0.5.seconds)
        }
        Log.d(TAG, "Sending message: $message")
        try {
            if (sendChannel == null) {
                Log.e(TAG, "Socket is not connected.")
                throw IllegalStateException("Socket is not connected.")
            }
            sendChannel!!.writeStringUtf8(message)
            Log.d(TAG, "sendMessage: message sent successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send message: ", e)
            Log.d(TAG, "sendMessage: Attempt to restart the socket connection")
            startSocket()
        }
    }

    private companion object {
        const val TAG = "FlorestaRpcKtor"
        private const val ELECTRUM_ADDRESS = "127.0.0.1"
        private const val RCP_PORT = 38332
    }
}
