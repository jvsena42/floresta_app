package com.github.jvsena42.floresta.domain.model.florestaRPC


import com.google.gson.annotations.SerializedName

data class GetBlockchainInfoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("jsonrpc")
    val jsonrpc: String,
    @SerializedName("result")
    val result: Result
)