package com.github.jvsena42.floresta.domain.floresta

import org.json.JSONObject
import org.json.JSONArray
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class FlorestaRpcImpl: FlorestaRpc {
    lateinit var host: String

    override fun rescan() {
        val arguments = JSONArray()
        arguments.put(0)
        sendJsonRpcRequest(
            host,
            "rescan",
            arguments
        )
    }

    override fun getPeerInfo() {
        val arguments = JSONArray()

        sendJsonRpcRequest(
            host,
            "getpeerinfo",
            arguments
        )
    }

    override fun getProgress() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        val arguments = JSONArray()

        sendJsonRpcRequest(
            host,
            "stop",
            arguments
        )
    }

    override fun getBlockchainInfo() {
        val arguments = JSONArray()

        sendJsonRpcRequest(
            host,
            "getblockchaininfo",
            arguments
        )
    }

    fun sendJsonRpcRequest(endpoint: String, method: String, params: JSONArray): JSONObject {
        val client = okhttp3.OkHttpClient()

        val jsonRpcRequest = JSONObject().apply {
            put("jsonrpc", "2.0")
            put("method", method)
            put("params", params)
            put("id", 1)
        }.toString()

        val requestBody = okhttp3.RequestBody.create(
                "application/json".toMediaTypeOrNull(), jsonRpcRequest);

        val request = okhttp3.Request.Builder()
            .url(endpoint)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()

        val body = response.body
        return JSONObject(body.toString())
    }

}