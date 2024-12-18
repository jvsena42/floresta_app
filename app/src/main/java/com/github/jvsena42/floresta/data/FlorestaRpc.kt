package com.github.jvsena42.floresta.data

import com.github.jvsena42.floresta.domain.model.florestaRPC.GetBlockchainInfoResponse
import com.github.jvsena42.floresta.domain.model.florestaRPC.GetPeerInfoResponse
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

interface FlorestaRpc {
    suspend fun getBlockchainInfo(): Flow<Result<GetBlockchainInfoResponse>>
    suspend fun rescan(): Flow<Result<JSONObject>>
    suspend fun loadDescriptor(descriptor: String): Flow<Result<JSONObject>>
    suspend fun stop(): Flow<Result<JSONObject>>
    suspend fun getPeerInfo(): Flow<Result<GetPeerInfoResponse>>
}