package com.github.jvsena42.floresta.domain.floresta

import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

interface FlorestaRpc {
    suspend fun getBlockchainInfo(): Flow<Result<JSONObject>>
    suspend fun rescan(): Flow<Result<JSONObject>>
    suspend fun getProgress(): Flow<Result<JSONObject>>
    suspend fun stop(): Flow<Result<JSONObject>>
    suspend fun getPeerInfo(): Flow<Result<JSONObject>>
}