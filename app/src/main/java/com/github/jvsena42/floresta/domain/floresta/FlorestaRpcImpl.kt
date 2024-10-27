package com.github.jvsena42.floresta.domain.floresta

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject

class FlorestaRpcImpl : FlorestaRpc {
    var host: String = "http://$ELECTRUM_ADDRESS"

    override suspend fun rescan(): Flow<Result<JSONObject>> = callbackFlow {
        Log.d(TAG, "rescan: ")
        val arguments = JSONArray()
        arguments.put(0)

        send(
            sendJsonRpcRequest(
                host,
                "rescan",
                arguments
            )
        )
    }

    override suspend fun getPeerInfo(): Flow<Result<JSONObject>> = callbackFlow {
        Log.d(TAG, "getPeerInfo: ")
        val arguments = JSONArray()

        send(
            sendJsonRpcRequest(
                host,
                "getpeerinfo",
                arguments
            )
        )
    }

    override suspend fun getProgress(): Flow<Result<JSONObject>> {
        TODO("Not yet implemented")
    }

    override suspend fun stop(): Flow<Result<JSONObject>> = callbackFlow {
        Log.d(TAG, "stop: ")
        val arguments = JSONArray()

        send(
            sendJsonRpcRequest(
                host,
                "stop",
                arguments
            )
        )
    }

    override suspend fun getBlockchainInfo(): Flow<Result<JSONObject>> = callbackFlow {
        Log.d(TAG, "getBlockchainInfo: ")
        val arguments = JSONArray()

        send(
            sendJsonRpcRequest(
                host,
                "getblockchaininfo",
                arguments
            )
        )
    }

    suspend fun sendJsonRpcRequest(
        endpoint: String,
        method: String,
        params: JSONArray
    ): Result<JSONObject> {
        Log.d(TAG, "sendJsonRpcRequest: ")
        return try {
            val client = okhttp3.OkHttpClient()

            val jsonRpcRequest = JSONObject().apply {
                put("jsonrpc", "2.0")
                put("method", method)
                put("params", params)
                put("id", 1)
            }.toString()

            val requestBody = okhttp3.RequestBody.create(
                "application/json".toMediaTypeOrNull(), jsonRpcRequest
            )

            val request = okhttp3.Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()

            val body = response.body
            Result.success(JSONObject(body.toString()))
        } catch (e: Exception) {
            Log.e(TAG, "sendJsonRpcRequest error:", e)
            Result.failure(e)
        }
    }

    private companion object {
        private const val TAG = "FlorestaRpcImpl"
        private const val ELECTRUM_ADDRESS = "127.0.0.1:50001"
    }

}