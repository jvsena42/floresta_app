package com.github.jvsena42.floresta.domain.floresta

import com.github.jvsena42.floresta.domain.floresta.FlorestaDaemonImpl.Companion.ELECTRUM_ADDRESS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import org.json.JSONArray
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class FlorestaRpcImpl : FlorestaRpc {
    var host: String = "http://$ELECTRUM_ADDRESS"

    override suspend fun rescan(): Flow<Result<JSONObject>> = callbackFlow {
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
        return try {
            Result.success(JSONObject(body.toString()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}