package com.github.jvsena42.floresta.domain.model.florestaRPC

enum class RpcMethods(val method: String) {
    RESCAN("rescan"),
    GET_PEER_INFO("getpeerinfo"),
    STOP("stop"),
    GET_BLOCKCHAIN_INFO("getblockchaininfo"),
}