package com.github.jvsena42.floresta.domain.floresta

import android.util.Log
import com.github.jvsena42.floresta.data.FlorestaRpc
import com.github.jvsena42.floresta.domain.model.florestaRPC.GetBlockchainInfoResponse
import com.github.jvsena42.floresta.domain.model.florestaRPC.GetPeerInfoResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class FlorestaRpcImpl(
    private val gson: Gson,
) : FlorestaRpc {
    var host: String = "http://$ELECTRUM_ADDRESS"

    override suspend fun rescan(): Flow<Result<JSONObject>> = flow {
        Log.d(TAG, "rescan: ")
        val arguments = JSONArray()
        arguments.put(0)

        emit(
            sendJsonRpcRequest(
                host,
                "rescan",
                arguments
            )
        )
    }

    override suspend fun getPeerInfo(): Flow<Result<GetPeerInfoResponse>> = flow {
        Log.d(TAG, "getPeerInfo: ")
        val arguments = JSONArray()

        sendJsonRpcRequest(
            host,
            "getpeerinfo",
            arguments
        ).fold(
            onSuccess = { json ->
                emit(
                    Result.success(
                        gson.fromJson(json.toString(), GetPeerInfoResponse::class.java)
                    )
                )
            },
            onFailure = { e ->
                Log.d(TAG, "getPeerInfo: failure")
                emit(Result.failure(e))
            }
        )
    }

    override suspend fun getProgress(): Flow<Result<JSONObject>> {
        TODO("Not yet implemented")
    }

    override suspend fun stop(): Flow<Result<JSONObject>> = flow {
        Log.d(TAG, "stop: ")
        val arguments = JSONArray()

        emit(
            sendJsonRpcRequest(
                host,
                "stop",
                arguments
            )
        )
    }

    override suspend fun getBlockchainInfo(): Flow<Result<GetBlockchainInfoResponse>> = flow {
        Log.d(TAG, "getBlockchainInfo: ")
        val arguments = JSONArray()

        sendJsonRpcRequest(
            host,
            "getblockchaininfo",
            arguments
        ).fold(
            onSuccess = { json ->
                emit(
                    Result.success(
                        gson.fromJson(
                            json.toString(),
                            GetBlockchainInfoResponse::class.java
                        )
                    )
                )
            },
            onFailure = { e ->
                emit(Result.failure(e))
            }
        )
    }

    suspend fun sendJsonRpcRequest(
        endpoint: String,
        method: String,
        params: JSONArray,
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

            val requestBody = jsonRpcRequest.toRequestBody("application/json".toMediaTypeOrNull())

            val request = okhttp3.Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()

            val body = response.body
            Result.success(JSONObject(body?.string().orEmpty()))
        } catch (e: Exception) {
            Log.e(TAG, "sendJsonRpcRequest error:", e)
            Result.failure(e)
        }
    }

    private companion object {
        private const val TAG = "FlorestaRpcImpl"
        private const val ELECTRUM_ADDRESS = "127.0.0.1:38332"
    }

}